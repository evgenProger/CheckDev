package ru.job4j.ci.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Task;

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
public class JobRepositoryTest {
    @Autowired
    private ProjectRepository projects;

    @Autowired
    private JobRepository jobs;

    @Autowired
    private TaskRepository tasks;

    @Test
    public void whenGetTaskThenFillType() {
        Job job = new Job();
        job.setName("Test");
        this.jobs.save(job);
        Task task = new Task();
        task.setName("Test");
        task.setType(Task.Type.MVN);
        task.setJob(job);
        this.tasks.save(task);
        assertThat(this.tasks.findById(task.getId()).get().getType(), is(Task.Type.MVN));
    }

    @Test
    public void whenGetJobThenFillType() {
        Job job = new Job();
        job.setName("Test");
        this.jobs.save(job);
        Task task = new Task();
        task.setName("Test");
        task.setType(Task.Type.MVN);
        task.setJob(job);
        this.tasks.save(task);
        assertThat(this.jobs.findById(job.getId()).get().getTasks().iterator().next().getType(), is(Task.Type.MVN));
    }
}