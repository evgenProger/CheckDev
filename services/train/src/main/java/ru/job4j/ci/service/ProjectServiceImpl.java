package ru.job4j.ci.service;

import com.google.common.collect.Lists;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.ci.domain.*;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.repository.ProjectRepository;
import ru.job4j.ci.repository.ProjectTaskRepository;
import ru.job4j.ci.repository.SettingRepository;
import ru.job4j.ci.task.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.util.*;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final StdSchedulerFactory factory = new StdSchedulerFactory();

    private final Map<Task.Type, TrainTask> tasks = new ConcurrentHashMap<>();

    private final ProjectRepository projects;

    private final ProjectTaskRepository ptasks;

    private final SettingRepository settings;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projects, ProjectTaskRepository ptasks, SettingRepository settings) {
        this.projects = projects;
        this.ptasks = ptasks;
        this.settings = settings;
        tasks.put(Task.Type.REFRESH, new RefreshTask());
        tasks.put(Task.Type.CLONE, new CloneTask());
        tasks.put(Task.Type.MVN, new MvnTask());
        tasks.put(Task.Type.UPDATE, new UpdateTask());
        tasks.put(Task.Type.CMD, new CmdTask());
    }

    @Override
    public List<Project> getAll() {
        return this.projects.findAllByOrderByNameAsc();
    }

    @Transactional
    @Override
    public Project save(Project project) throws SchedulerException, ParseException {
        this.projects.save(project);
        this.ptasks.removeByProject(project);
        for (Job job : project.getJobs()) {
            if (job != null) {
               for (Task task : job.getTasks()) {
                    this.ptasks.save(new ProjectTask(
                            task,
                            project,
                            Calendar.getInstance(),
                            ProjectTask.Result.NEW
                    ));
                }
            }
        }
        this.cronRun(project);
        return project;
    }

    public ProjectTask getProjectTask(int id) {
        return this.ptasks.findById(id).get();
    }

    public void run(int projectId) throws ParseException, SchedulerException {
        final Project project = this.projects.findById(projectId).get();
        boolean hasCron = false;
        for (Job job : project.getJobs()) {
            if (job != null && job.getCron() != null) {
                hasCron = true;
                break;
            }
        }
        if (hasCron) {
            cronRun(project);
        } else {
            directRun(project);
        }
    }

    private void directRun(final Project project) {
        final List<Setting> sets = Lists.newArrayList(this.settings.findAll());
        project.setStatus(Project.Status.SUSPECT);
        this.projects.save(project);
        this.pool.execute(
                () -> {
                    project.setStatus(Project.Status.RUN);
                    this.projects.save(project);
                    for (ProjectTask task : project.getTasks()) {
                        TrainTask target = ProjectServiceImpl.this.tasks.get(task.getTask().getType());
                        if (target.before(task)) {
                            Log log = target.execute(sets, project, task.getTask());
                            if (target.after(task, log)) {
                                break;
                            }
                            ProjectServiceImpl.this.updateLog(task, log);
                        }
                    }
                    project.setStatus(Project.Status.STOP);
                    this.projects.save(project);
                }
        );
    }

    private void updateLog(ProjectTask task, Log log) {
        task.setResult(log.isSuccess() ? ProjectTask.Result.SUCCESS : ProjectTask.Result.FAIL);
        task.setTime(Calendar.getInstance());
        task.setLog(log.getLog());
        task.setValue(String.valueOf(log.isSuccess()));
        this.ptasks.save(task);
    }

    @Override
    public Project findOne(int projectId) {
        return this.projects.findById(projectId).get();
    }


    @Transactional
    @Override
    public void delete(int id) {
        this.ptasks.removeByProject(new Project(id));
        this.projects.deleteById(id);
    }

    @PostConstruct
    public void load() throws SchedulerException, ParseException {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", "1");
        this.factory.initialize(properties);
        Scheduler scheduler = this.factory.getScheduler();
        scheduler.start();
        for (Project project : this.projects.findAll()) {
            this.cronRun(project);
        }
    }

    private void cronRun(final Project project) throws ParseException, SchedulerException {
        for (Job job : project.getJobs()) {
            if (job != null && job.getCron() != null) {
                this.factory.getScheduler().deleteJob(new JobKey(String.valueOf(job.getId())));
                JobDetailImpl detail = new JobDetailImpl();
                detail.setName(String.valueOf(job.getId()));
                detail.setJobClass(ScheduleJob.class);
                JobDataMap dataMap = new JobDataMap();
                dataMap.put("project", project);
                dataMap.put("service", this);
                detail.setJobDataMap(dataMap);
                CronTriggerImpl trigger = new CronTriggerImpl();
                trigger.setName(String.valueOf(job.getId()));
                trigger.setCronExpression(job.getCron());
                this.factory.getScheduler().scheduleJob(detail, trigger);
            }
        }
    }

    @PreDestroy
    public void stop() throws SchedulerException {
        this.pool.shutdown();
        for (Scheduler scheduler : this.factory.getAllSchedulers()) {
            scheduler.shutdown();
        }
    }

    public static final class ScheduleJob implements org.quartz.Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Project project = (Project) context.getJobDetail().getJobDataMap().get("project");
            ProjectServiceImpl service = (ProjectServiceImpl) context.getJobDetail().getJobDataMap().get("service");
            service.directRun(project);
        }
    }

}
