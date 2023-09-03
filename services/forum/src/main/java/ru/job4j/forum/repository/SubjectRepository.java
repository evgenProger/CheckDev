package ru.job4j.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.forum.domain.Subject;

import java.util.Calendar;

/**
 * Repository used to manipulate forum subjects in database.
 * Implementation is automatically created by Spring Data.
 * It can use {@link org.springframework.data.domain.Pageable} parameter in its methods to perform pagination.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface SubjectRepository extends PagingAndSortingRepository<Subject, Integer> {

    /**
     * Find subjects in specified category.
     *
     * @param categoryId id of category which contains returned subjects.
     * @param pageable object used for pagination of result.
     * @return list of found subjects.
     */
    Page<Subject> findByCategoryId(int categoryId, Pageable pageable);

    /**
     * Find all subjects with a lastDate not later than the specified date.
     *
     * @param calendar Date no later than which you need to find the subjects.
     * @param pageable object used for pagination of result.
     * @return list of found subjects.
     */
    Page<Subject> findAllByLastDateAfterOrderByLastDateDesc(Calendar calendar, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update subject set last_user_key = ?2, last_date = ?3, count_message = count_message + 1 " +
            "where subject_id = ?1")
    void updateLastMessage(int subjectId, String lastUserKey, Calendar lastDate);

    @Transactional
    @Modifying
    @Query("update subject set count_view = count_view + 1 where subject_id = ?1")
    void updateCountView(int subjectId);

    @Transactional
    @Modifying
    @Query("update subject set last_user_key = ?2, last_date = ?3, count_message = count_message - 1 " +
            "where subject_id = ?1")
    void updateLastMessageAfterDeleteLastMessage(int subjectId, String lastUserKey, Calendar lastDate);
}