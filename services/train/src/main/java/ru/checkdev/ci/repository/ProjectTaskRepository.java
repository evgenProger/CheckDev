package ru.checkdev.ci.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.ci.domain.Project;
import ru.checkdev.ci.domain.ProjectTask;
import ru.checkdev.ci.domain.Task;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Integer> {
    @Transactional
    void removeByProject(Project project);

    @Transactional
    void removeByTask(Task task);

    List<ProjectTask> findAllByTask(Task task);
}
