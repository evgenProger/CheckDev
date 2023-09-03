package ru.job4j.ci.service;

import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Project;

import java.text.ParseException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
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
public class ProjectServiceTest {
    @Autowired
    private ProjectService projects;

    @Autowired
    private JobService jobs;

    @Test
    public void whenThen() throws ParseException, SchedulerException {
        this.projects.run(11);
    }

    @Test
    public void whenFindThen() {
        Project project = this.projects.findOne(11);
        assertThat(project.getTasks().size(), is(3));
    }

    @Test
    public void whenDeleteThen() throws ParseException, SchedulerException {
        Job job = this.jobs.findOne(2);
        Project project = new Project();
        project.setName("Anton Viktorov");
        project.setUrl("git@github.com:antviktorov/java-courses.git");
        projects.save(project);
        project.setJobs(Arrays.asList(job));
        projects.save(project);
        assertThat(this.projects.findOne(project.getId()).getTasks().isEmpty(), is(false));
    }
}