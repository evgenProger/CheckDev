package ru.job4j.exam.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.AOpt;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface AOptRepository extends CrudRepository<AOpt, Integer> {

    @Modifying
    @Transactional
    @Query("delete from answer_opt o where o.answer.id = ?1")
    void deleteByAnswerId(int answerId);

    @Modifying
    @Transactional
    @Query("delete from answer_opt o where o.opt.id in (select id from question_opt o where o.question.exam.id = ?1)")
    void deleteByExamId(int examId);

    @Modifying
    @Transactional
    @Query("delete from answer_opt o where o.answer.id in (select id from answer a where a.user.id = ?1)")
    void deleteByExamUserId(int userId);
}
