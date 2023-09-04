package ru.checkdev.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.interview.domain.Report;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ReportRepository extends CrudRepository<Report, Integer> {
}
