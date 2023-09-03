package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.ITask;
import ru.job4j.interview.domain.Interview;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ITaskRepository extends CrudRepository<ITask, Integer> {
    List<ITask> findByInterviewIdIn(Collection<Integer> ids);

    List<ITask> findByInterviewId(int interviewId);

    @Modifying
    @Transactional
    @Query("delete from interview_task t where t.task.id = ?1")
    void deleteByTask(int taskId);

    @Modifying
    @Transactional
    @Query("delete from interview_task i where i.interview.id = ?1")
    void deleteByInterviewId(int id);
}
