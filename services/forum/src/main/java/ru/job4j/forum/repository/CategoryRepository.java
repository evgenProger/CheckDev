package ru.checkdev.forum.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.checkdev.forum.domain.Category;

/**
 * Repository used to manipulate forum categories in database.
 * Implementation is automatically created by Spring Data.
 * It can use {@link org.springframework.data.domain.Pageable} parameter in its methods to perform pagination.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
}