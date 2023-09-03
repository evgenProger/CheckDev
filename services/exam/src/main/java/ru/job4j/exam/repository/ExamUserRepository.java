package ru.job4j.exam.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.ExamUser;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ExamUserRepository extends CrudRepository<ExamUser, Integer> {
    ExamUser findByKeyAndExamId(String key, int examId);

    List<ExamUser> findByKey(String key);

    @Query("select e.id, count(e.id), sum(u.result)/count(e.id) from exam_user as u join u.exam as e on e.active is true group by e.id")
    List<Object[]> getAverageInfo();

    @Query("select count(e.id), sum(u.result)/count(e.id) from exam_user as u join u.exam as e on e.active is true and e.id = ?1")
    List<Object[]> getAverageInfo(int examId);

    @Query("select u from exam_user as u where u.exam.id = ?1 order by u.result desc")
    Iterable<ExamUser> getMax(int examId);

    @Modifying
    @Transactional
    @Query("delete from exam_user e where e.exam.id = ?1")
    void deleteByExamId(int examId);
}
