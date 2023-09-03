package ru.job4j.ci.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.ProjectTask;
import ru.job4j.ci.domain.Task;

import java.util.List;

import static org.hamcrest.core.Is.is;
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
public class ProjectTaskRepositoryTest {
    @Autowired
    private ProjectTaskRepository pts;

    @Test
    public void whenThen() {
        final int taskId = 1;
        List<ProjectTask> tasks = this.pts.findAllByTask(new Task(taskId, null, null));
        assertThat(tasks.iterator().next().getTask().getId(), is(taskId));
    }
}