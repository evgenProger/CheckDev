package ru.checkdev.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.interview.domain.Member;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface MemberRepository extends CrudRepository<Member, Integer> {
}
