package ru.checkdev.ci.service;

import org.quartz.SchedulerException;
import ru.checkdev.ci.domain.Project;
import ru.checkdev.ci.domain.ProjectTask;

import java.text.ParseException;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ProjectService {
    List<Project> getAll();

    Project save(Project project) throws SchedulerException, ParseException;

    void run(int project) throws ParseException, SchedulerException;

    Project findOne(int projectId);

    void delete(int id);

    ProjectTask getProjectTask(int id);
}
