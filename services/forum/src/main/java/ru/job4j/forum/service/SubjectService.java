package ru.checkdev.forum.service;

import org.springframework.data.domain.Page;
import ru.checkdev.forum.domain.Subject;

/**
 * Service used to perform actions on subjects.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface SubjectService {

    /**
     * Get all subjects in given category.
     *
     * @param categoryId id of category that contains returned subjects.
     * @param page page to return.
     * @return list of found subjects.
     * @throws Exception thrown if can't get subjects or its authors.
     */
    Page<Subject> getAll(int categoryId, int page) throws Exception;

    /**
     * Get all subjects.
     *
     * @param page page to return.
     * @return list of found subjects.
     * @throws Exception thrown if can't get subjects or its authors.
     */
    Page<Subject> getAllSubject(int page) throws Exception;

    /**
     * Get subject by specific id.
     *
     * @param id subject's id.
     * @return found subject.
     * @throws Exception thrown if can't get subject or its author.
     */
    Subject getById(int id) throws Exception;

    /**
     * Save subject (add or update).
     *
     * @param subject subject object to save.
     * @return saved subject.
     */
    Subject save(Subject subject);

    /**
     * Delete subject.
     *
     * @param id id of subject to delete.
     */
    void delete(int id);

    /**
     * Increment number of views for subject.
     *
     * @param id subject's id.
     */
    void updateCountView(int id);
}