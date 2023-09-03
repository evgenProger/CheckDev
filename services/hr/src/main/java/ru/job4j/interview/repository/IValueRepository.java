package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.IValue;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface IValueRepository extends CrudRepository<IValue, Integer> {

    @Modifying
    @Transactional
    @Query("delete from interview_value v where v.task.id in (select t.id from interview_task as t where t.task.id = ?1)")
    void deleteByTask(int taskId);

    @Modifying
    @Transactional
    @Query("delete from interview_value v where v.task.id in (select t.id from interview_task as t where t.interview.id = ?1)")
    void deleteByInterviewId(int id);
}
