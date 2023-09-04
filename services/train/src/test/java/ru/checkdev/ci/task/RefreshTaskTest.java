package ru.checkdev.ci.task;

import org.junit.Ignore;
import org.junit.Test;
import ru.checkdev.ci.domain.Log;
import ru.checkdev.ci.domain.Project;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.checkdev.ci.domain.Setting;
import ru.checkdev.ci.domain.Task;

import java.util.Arrays;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
public class RefreshTaskTest {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTaskTest.class);
    private static final String URL = "https://github.com/antviktorov/java-courses.git";

    private static final List<Setting> STGS = Arrays.asList(
            new Setting("c:\\tmp\\students", Setting.Type.WORKSPACE),
            new Setting("job4j", Setting.Type.GIT_USER),
            new Setting("job4j", Setting.Type.GIT_PWD),
            new Setting("c:\\Tools\\apache-maven-3.3.9\\", Setting.Type.MVN_HOME)
    );

    @Test
    public void whenExecCloneThenCompareLastRev() {
        Project project = mock(Project.class);
        when(project.getUrl()).thenReturn(URL);
        when(project.getName()).thenReturn("Anton Viktorov");
        Log log = new CloneTask().execute(STGS, project, null);
        assertThat(log.getLog(), log.isSuccess(), is(true));
        LOG.info(log.getLog());
    }

    @Test
    public void whenExecFetchThenCompareLastRev() {
        Project project = mock(Project.class);
        when(project.getUrl()).thenReturn(URL);
        Log log = new UpdateTask().execute(STGS, project, null);
        assertThat(log.getLog(), log.isSuccess(), is(true));
        LOG.info(log.getLog());
    }


    @Test
    public void whenExecBuildThenCompareLastRev() {
        Project project = mock(Project.class);
        when(project.getUrl()).thenReturn(URL);
        Log log = new RefreshTask().execute(STGS, project, null);
        assertThat(log.getLog(), log.isSuccess(), is(true));
        LOG.info(log.getLog());
    }

    @Test
    public void whenExecMvnThenBuild() {
        Project project = mock(Project.class);
        when(project.getName()).thenReturn("Anton Viktorov");
        when(project.getUrl()).thenReturn(URL);
        Task task = new Task();
        task.setCommand("clean verify");
        Log log = new MvnTask().execute(STGS, project, task);
        LOG.info(log.getLog());
        assertThat(log.getLog(), log.isSuccess(), is(true));
    }
}