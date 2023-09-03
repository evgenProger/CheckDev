package ru.job4j.ci.task;

import ru.job4j.ci.domain.*;
import ru.job4j.ci.service.Cmd;
import ru.job4j.ci.service.Mvn;
import ru.job4j.ci.service.Services;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TrainTask {
    Cmd CMD = new Cmd();
    Mvn MVN = new Mvn();
    String NL = System.getProperty("line.separator");

    Log execute(List<Setting> settings, Project project, Task task);

    default String projectName(String url) {
        return url != null && !url.isEmpty() ? url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")) : "";
    }

    default String setting(List<Setting> settings, Setting.Type type) {
        return settings.stream()
                .filter(it -> it.getType() == type)
                .findFirst().get().getValue();
    }

    default boolean before(ProjectTask value) {
        return true;
    }

    default boolean after(ProjectTask value, Log log) {
        return false;
    }
}
