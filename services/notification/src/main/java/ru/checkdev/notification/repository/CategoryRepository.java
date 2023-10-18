package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Override
    @EntityGraph(attributePaths = {"user"})
    List<Category> findAll();

    @Override
    @EntityGraph(attributePaths = {"user"})
    Category save(Category category);
}
