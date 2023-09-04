package ru.checkdev.ci.service;

import org.junit.Ignore;
import org.junit.Test;
import org.quartz.SchedulerException;
import ru.checkdev.ci.domain.*;
import ru.checkdev.ci.repository.ProjectRepository;
import ru.checkdev.ci.repository.ProjectTaskRepository;
import ru.checkdev.ci.repository.SettingRepository;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
public class Exec {
    private static final String URL = "https://github.com/vrnsky/java-a-to-z";

    @Test
    public void whenThen() throws InterruptedException, ParseException, SchedulerException {
        final List<Setting> STGS = Arrays.asList(
                new Setting("c:\\tmp\\students", Setting.Type.WORKSPACE),
                new Setting("job4j", Setting.Type.GIT_USER),
                new Setting("job4j", Setting.Type.GIT_PWD),
                new Setting("", Setting.Type.MVN_HOME)
        );
        ProjectRepository projects = mock(ProjectRepository.class);
        ProjectTaskRepository tasks = mock(ProjectTaskRepository.class);
        SettingRepository settings = mock(SettingRepository.class);
        when(settings.findAll()).thenReturn(
                new Iterable<Setting>() {
                    @Override
                    public Iterator<Setting> iterator() {
                        return STGS.iterator();
                    }
                }
        );
        Job job = new Job();
        Project project = new Project();
        project.setName("Anton Viktorov");
        project.setUrl(URL);
        project.setTasks(
                Arrays.asList(
                        new ProjectTask(new Task(-1, Task.Type.CLONE, ""), null, Calendar.getInstance(), ProjectTask.Result.NEW),
                        new ProjectTask(new Task(-1, Task.Type.UPDATE, ""), null, Calendar.getInstance(), ProjectTask.Result.NEW),
                        new ProjectTask(new Task(-1, Task.Type.REFRESH, ""), null, Calendar.getInstance(), ProjectTask.Result.NEW),
                        new ProjectTask(new Task(-1, Task.Type.MVN, "clean"), null, Calendar.getInstance(), ProjectTask.Result.NEW)
        )
        );
        project.setJobs(Collections.singletonList(job));
        when(projects.findById(1).get()).thenReturn(project);
        ProjectServiceImpl service = new ProjectServiceImpl(projects, tasks, settings);
        service.run(1);
    }
}