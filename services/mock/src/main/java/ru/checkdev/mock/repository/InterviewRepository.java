package ru.checkdev.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.mock.domain.Interview;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    Optional<Interview> findById(int id);

    List<Interview> findByMode(int mode);

    Page<Interview> findByTopicId(int topicId, Pageable pageable);

    /**
     * Метод обновляет статус собеседования.
     *
     * @param id     ID Interview
     * @param status Status
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE interview i SET i.status=:status WHERE i.id=:id")
    void updateStatus(@Param("id") int id, @Param("status") int status);

    Page<Interview> findByTopicIdIn(Collection<Integer> topicIds, Pageable pageable);

    /**
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью, автором которых является пользователь
     */
    Page<Interview> findBySubmitterId(int submitterId, Pageable pageable);

    /**
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью, автором которых пользователь НЕ является
     */
    Page<Interview> findBySubmitterIdNot(int submitterId, Pageable pageable);

    /**
     * @param topicId int
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью определённой темы, автором которых является пользователь
     */
    Page<Interview> findByTopicIdAndSubmitterId(
            int topicId,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicIds Collection<Integer>
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью определённой категории, автором которых является пользователь
     */
    Page<Interview> findByTopicIdInAndSubmitterId(
            Collection<Integer> topicIds,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicId int
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью определённой темы, автором которых пользователь НЕ является
     */
    Page<Interview> findByTopicIdAndSubmitterIdNot(
            int topicId,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicIds Collection<Integer>
     * @param submitterId int
     * @param pageable Pageable
     * @return интервью определённой категории, автором которых пользователь НЕ является
     */
    Page<Interview> findByTopicIdInAndSubmitterIdNot(
            Collection<Integer> topicIds,
            int submitterId,
            Pageable pageable);

    /**
     * @param userId int
     * @param pageable Pageable
     * @return интервью, в которых пользователь НЕ участвует
     */
    @Query("SELECT i FROM interview i WHERE i.id NOT IN"
            + " (SELECT w.interview.id FROM wisher w WHERE w.userId = :userId)")
    Page<Interview> findInterviewByUserIdNot(@Param("userId") int userId, Pageable pageable);

    /**
     * @param userId int
     * @param topicId int
     * @param pageable Pageable
     * @return интервью определённой темы, в которых пользователь НЕ участвует
     */
    @Query("SELECT i FROM interview i WHERE i.id NOT IN"
            + " (SELECT w.interview.id FROM wisher w WHERE w.userId = :userId)"
            + " AND i.topicId = :topicId")
    Page<Interview> findInterviewByUserIdNotAndByTopicId(@Param("userId") int userId,
                                                         @Param("topicId") int topicId,
                                                         Pageable pageable);

    /**
     * @param userId int
     * @param topicsIds Collection<Integer>
     * @param pageable Pageable
     * @return интервью определённой категории, в которых пользователь НЕ участвует
     */
    @Query("SELECT i FROM interview i WHERE i.id NOT IN"
            + " (SELECT w.interview.id FROM wisher w WHERE w.userId = :userId)"
            + " AND i.topicId IN :topicsIds")
    Page<Interview> findInterviewByUserIdNotAndByTopicIdIn(
            @Param("userId") int userId,
            @Param("topicsIds") Collection<Integer> topicsIds,
            Pageable pageable);
}
