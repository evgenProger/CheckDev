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
public class CmdTask implements TrainTask {

    @Override
    public Log execute(List<Setting> settings, Project project, Task task) {
        String path = Joiner.on(File.separator).join(
                this.setting(settings, Setting.Type.WORKSPACE),
                project.getName(),
                projectName(project.getUrl())
        );
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CMD.exec(
                path,
                Arrays.asList(task.getCommand().split(" ")),
                NL
        );
    }
}
