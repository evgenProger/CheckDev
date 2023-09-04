package ru.checkdev.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.interview.domain.Comment;

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
