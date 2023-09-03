package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.domain.Role;

/**
 *
 * @author parsentev
 * @since 25.09.2016
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
