package ru.job4j.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.TAlgo;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TAlgoRepository extends CrudRepository<TAlgo, Integer> {
    List<TAlgo> findByPublished(boolean published);
}
