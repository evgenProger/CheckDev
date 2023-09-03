package ru.job4j.exam.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.QOpt;

import java.util.List;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface QOptRepository extends CrudRepository<QOpt, Integer> {
    List<QOpt> findByQuestionIdOrderByPosAsc(int questionId);

    @Modifying
    @Transactional
    @Query("delete from question_opt o where o.question.id in (select id from question q where q.exam.id = ?1)")
    void deleteByExamId(int examId);
}
