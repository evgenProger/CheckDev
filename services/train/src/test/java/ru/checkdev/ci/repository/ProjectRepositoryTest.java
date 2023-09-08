package ru.checkdev.ci.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.ci.domain.Job;
import ru.checkdev.ci.domain.Project;
import ru.checkdev.ci.domain.ProjectTask;
import ru.checkdev.ci.domain.Task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "test" })
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projects;

    @Autowired
    private JobRepository jobs;

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private ProjectTaskRepository pts;

    @Test
    public void whenFindByJobThen() {
        List<Project> list = this.projects.findAllByJobs(new Job(1));
        assertThat(list.isEmpty(), is(false));
    }

    @Test
    public void whenThen() {
        Job job = new Job();
        job.setName("Java A to Z");
        job = this.jobs.save(job);
        Task task = new Task();
        task.setJob(job);
        task.setType(Task.Type.MVN);
        task = this.tasks.save(task);
        Project project = new Project();
        project.setName("Anton Viktorov");
        project.setUrl("git@github.com:antviktorov/java-courses.git");
        this.projects.save(project);
        project.setJobs(Arrays.asList(job));
        this.projects.save(project);

        ProjectTask pt = new ProjectTask();
        pt.setProject(project);
        pt.setTask(task);
        pt.setResult(ProjectTask.Result.NEW);
        pt.setTime(Calendar.getInstance());
        this.pts.save(pt);
        assertThat(project.getId(), is(notNullValue()));
        assertThat(projects.findById(project.getId()).get().getJobs().isEmpty(), is(false));
        this.pts.removeByProject(new Project(project.getId()));
    }
}