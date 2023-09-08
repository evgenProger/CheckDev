package ru.checkdev.ci.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.ci.domain.Job;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface JobRepository extends CrudRepository<Job, Integer> {
}
