package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.Comment;
import ru.job4j.interview.domain.Team;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findByOwner(String owner);

    Team findByIdAndOwner(int teamId, String key);

    @Query("select t.id from team as t join t.members as m on m.person = ?1")
    List<Integer> findByMemberKey(String key);
}
