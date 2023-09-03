package ru.job4j.exam.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.Question;

import java.util.List;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByExamIdOrderByPosAsc(int examId);

    List<Question> findByExamId(int examId, Pageable page);

    Long countByExamId(int examId);

    @Modifying
    @Transactional
    @Query("delete from question q where q.exam.id = ?1")
    void deleteByExamId(int examId);
}
