package ru.job4j.ci.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Project;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ProjectRepository extends CrudRepository<Project, Integer> {
    List<Project> findAllByOrderByNameAsc();

    List<Project> findAllByJobs(Job job);
}
