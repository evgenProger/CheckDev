package ru.checkdev.ci.task;

import com.google.common.base.Joiner;
import ru.checkdev.ci.domain.Log;
import ru.checkdev.ci.domain.Project;
import ru.checkdev.ci.domain.Setting;
import ru.checkdev.ci.domain.Task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UpdateTask implements TrainTask {
    public Log execute(List<Setting> settings, Project project, Task task) {
        return CMD.exec(
                Joiner.on("").join(
                        this.setting(settings, Setting.Type.WORKSPACE),
                        File.separator,
                        project.getName(),
                        File.separator,
                        projectName(project.getUrl())
                ),
                Arrays.asList("git", "pull", "--rebase"),
                NL
        );
    }
}
