package ru.job4j.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.Report;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ReportRepository extends CrudRepository<Report, Integer> {
}
