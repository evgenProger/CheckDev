package ru.job4j.interview.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.Comment;
import ru.job4j.interview.domain.Interview;

import java.util.Calendar;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findByInterviewIdOrderByCreated(int interviewId);

    void deleteByInterviewId(int id);
}
