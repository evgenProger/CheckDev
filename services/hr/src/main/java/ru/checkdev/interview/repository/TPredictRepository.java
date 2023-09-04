package ru.checkdev.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.interview.domain.TPredict;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TPredictRepository extends CrudRepository<TPredict, Integer> {
    @Modifying
    @Transactional
    @Query("delete from task_predict p where p.id not in (?1) and p.task.id = ?2")
    void deleteNotIn(List<Integer> ids, int taskId);

    @Modifying
    @Transactional
    @Query("delete from task_predict p where p.task.id = ?1")
    void deleteByTask(int taskId);

    List<TPredict> findByTaskId(int id);
}
