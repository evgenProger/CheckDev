package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.TValue;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TValueRepository extends CrudRepository<TValue, Integer> {
    @Modifying
    @Transactional
    @Query("delete from task_value v where v.id not in (?1) and v.task.id = ?2")
    void deleteNotIn(List<Integer> ids, int taskId);

    @Modifying
    @Transactional
    @Query("delete from task_value v where v.task.id = ?1")
    void deleteByTask(int taskId);

    List<TValue> findByTaskId(int id);
}
