package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    @EntityGraph(attributePaths = {"category"})
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = {"category"})
    User save(User user);
}
