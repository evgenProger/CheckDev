package ru.job4j.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.Comment;
import ru.job4j.interview.domain.Member;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface MemberRepository extends CrudRepository<Member, Integer> {
}
