package ru.checkdev.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.interview.domain.Task;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query("select t from task as t where t.vacancy.id = ?1")
    List<Task> findByVacancyIdByPosAsc(int vacancyId);

    @Modifying
    @Transactional
    @Query("update task t set t.pos = ?2 where t.id = ?1")
    void updatePosition(int currentId, int pos);
}
