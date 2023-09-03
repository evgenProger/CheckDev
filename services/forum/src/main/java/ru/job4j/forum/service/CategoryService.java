package ru.job4j.forum.service;

import ru.job4j.forum.domain.Category;

import java.util.List;

/**
 * Service used to perform actions on categories.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface CategoryService {

    /**
     * Get all categories.
     *
     * @return list of found categories.
     */
    List<Category> getAll();

    /**
     * Get category with specified id.
     *
     * @param id category's id.
     * @return found category.
     */
    Category getById(int id);

    /**
     * Save category (add or update).
     *
     * @param category category object to save.
     * @return saved category.
     */
    Category save(Category category);

    /**
     * Delete category.
     *
     * @param id id of category to delete.
     */
    void delete(int id);
}