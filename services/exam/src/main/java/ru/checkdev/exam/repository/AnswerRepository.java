package ru.checkdev.exam.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.exam.domain.Answer;

import java.util.List;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface AnswerRepository extends CrudRepository<Answer, Integer> {
    Answer findByUserIdAndQuestionId(int userId, int questionId);

    List<Answer> findByUserId(int userId);

    @Modifying
    @Transactional
    @Query("delete from answer a where a.question.id in (select id from question q where q.exam.id = ?1)")
    void deleteByExamId(int examId);
}

