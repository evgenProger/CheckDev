package ru.job4j.ci.task;

import com.google.common.base.Joiner;
import ru.job4j.ci.domain.Log;
import ru.job4j.ci.domain.Project;
import ru.job4j.ci.domain.Setting;
import ru.job4j.ci.domain.Task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MvnTask implements TrainTask {
    @Override
    public Log execute(List<Setting> settings, Project project, Task task) {
        return MVN.exec(
                this.setting(settings, Setting.Type.MVN_HOME),
                Joiner.on("").join(
                        this.setting(settings, Setting.Type.WORKSPACE),
                        File.separator,
                        project.getName(),
                        File.separator,
                        projectName(project.getUrl())
                ),
                Arrays.asList(task.getCommand().split(" "))
        );
    }
}
