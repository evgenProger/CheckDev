package ru.checkdev.forum.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.forum.domain.Message;
import ru.checkdev.forum.domain.Subject;

import java.sql.ResultSet;
import java.util.List;

/**
 * Repository used to manipulate forum messages in database.
 * Implementation is automatically created by Spring Data.
 * It can use {@link org.springframework.data.domain.Pageable} parameter in its methods to perform pagination.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {

    /**
     * Find messages in specified subject.
     *
     * @param subjectId id of subject which contains returned messages.
     * @param sort object used for sorting of result.
     * @return list of found messages.
     */
    Iterable<Message> findBySubjectId(int subjectId, Sort sort);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT*FROM message " +
            "WHERE create_date IN " +
            "      (SELECT max(create_Date)" +
            "       FROM message" +
            "       WHERE subject_id = ?1)")
    Message findByCreateDate(int subject);

}