package ru.job4j.ci.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.ci.domain.Project;
import ru.job4j.ci.domain.ProjectTask;
import ru.job4j.ci.domain.Task;

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
