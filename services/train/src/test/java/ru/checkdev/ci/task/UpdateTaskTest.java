package ru.checkdev.ci.task;

import org.junit.Ignore;
import org.junit.Test;
import ru.checkdev.ci.domain.Log;
import ru.checkdev.ci.domain.Project;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
public class UpdateTaskTest {
    @Test
    public void whenExecUpdateShouldUpdateIt() {
        Project project = mock(Project.class);
        when(project.getUrl()).thenReturn("git@github.com:antviktorov/java-courses.git");
        Log log = new UpdateTask().execute(null, project, null);
        assertThat(log.getLog(), log.isSuccess(), is(true));
    }
}