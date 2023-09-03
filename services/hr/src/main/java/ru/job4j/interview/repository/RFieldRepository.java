package ru.job4j.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.RField;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface RFieldRepository extends CrudRepository<RField, Integer> {
}
