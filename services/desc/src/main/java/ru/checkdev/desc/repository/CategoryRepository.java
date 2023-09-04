package ru.checkdev.desc.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.desc.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
