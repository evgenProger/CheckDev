package ru.checkdev.desc.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.desc.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Iterable<Category> findAllByOrderByTotalDesc();

    Iterable<Category> findTop5AllByOrderByTotalDesc();

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Modifying
    @Query("UPDATE cd_category c SET total = total + 1 WHERE c.id = :id")
    void updateStatistic(int id);
}
