package ru.job4j.ci.task;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import ru.job4j.ci.domain.*;
import ru.job4j.ci.service.Cmd;
import ru.job4j.ci.service.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CloneTask implements TrainTask {
    public Log execute(List<Setting> settings, Project project, Task task) {
        File file = new File(
                Joiner.on("").join(
                        this.setting(settings, Setting.Type.WORKSPACE),
                        File.separator,
                        project.getName()
                )
        );
        Log log;
        try {
            if (file.exists()) {
                Files.deleteIfExists(file.toPath());
            }
            if (file.mkdir()) {
                String url = project.getUrl();
                url = String.format(
                        "https://%s:%s@%s",
                        this.setting(settings, Setting.Type.GIT_USER),
                        this.setting(settings, Setting.Type.GIT_PWD),
                        url.substring("https://".length())
                );
                log = CMD.exec(
                        file.getAbsolutePath(),
                        Arrays.asList("git", "clone", url),
                        NL
                );
            } else {
                log = new Log(false, String.format("Folder %s could not created", file.getAbsoluteFile()));
            }
        } catch (IOException e) {
            log = new Log(false, Throwables.getStackTraceAsString(e));
        }
        return log;
    }

    @Override
    public boolean before(ProjectTask value) {
        return !"true".equals(value.getValue());
    }
}
