package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.Comment;
import ru.job4j.interview.domain.Permission;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface PermissionRepository extends CrudRepository<Permission, Integer> {
    List<Permission> findByTeamIdIn(List<Integer> ids);
}
