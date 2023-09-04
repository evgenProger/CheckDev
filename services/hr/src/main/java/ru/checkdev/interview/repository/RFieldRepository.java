package ru.checkdev.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.interview.domain.RField;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface RFieldRepository extends CrudRepository<RField, Integer> {
}
