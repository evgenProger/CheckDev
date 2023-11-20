package ru.checkdev.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.enums.StatusInterview;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    @Query("SELECT i FROM interview i"
            + " WHERE i.submitterId=:userId"
            + " OR i.status=:status"
            + " OR i.id IN (:interviewIds)"
    )
    Page<Interview> findAllByUserIdRelated(@Param("userId") int userId,
                                           @Param("status") StatusInterview status,
                                           @Param("interviewIds") List<Integer> interviewIds,
                                           Pageable pageable);

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
    void updateStatus(@Param("id") int id, @Param("status") StatusInterview status);

    Page<Interview> findByTopicIdIn(Collection<Integer> topicIds, Pageable pageable);

    /**
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью, автором которых является пользователь
     */
    Page<Interview> findBySubmitterId(int submitterId, Pageable pageable);

    /**
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью, автором которых пользователь НЕ является
     */
    Page<Interview> findBySubmitterIdNot(int submitterId, Pageable pageable);

    /**
     * @param topicId     int
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью определённой темы, автором которых является пользователь
     */
    Page<Interview> findByTopicIdAndSubmitterId(
            int topicId,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicIds    Collection<Integer>
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью определённой категории, автором которых является пользователь
     */
    Page<Interview> findByTopicIdInAndSubmitterId(
            Collection<Integer> topicIds,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicId     int
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью определённой темы, автором которых пользователь НЕ является
     */
    Page<Interview> findByTopicIdAndSubmitterIdNot(
            int topicId,
            int submitterId,
            Pageable pageable);

    /**
     * @param topicIds    Collection<Integer>
     * @param submitterId int
     * @param pageable    Pageable
     * @return интервью определённой категории, автором которых пользователь НЕ является
     */
    Page<Interview> findByTopicIdInAndSubmitterIdNot(
            Collection<Integer> topicIds,
            int submitterId,
            Pageable pageable);

    /**
     * @param userId   int
     * @param pageable Pageable
     * @return интервью, в которых пользователь НЕ участвует
     */
    @Query("SELECT i FROM interview i WHERE i.id NOT IN"
            + " (SELECT w.interview.id FROM wisher w WHERE w.userId = :userId)")
    Page<Interview> findInterviewByUserIdNot(@Param("userId") int userId, Pageable pageable);

    /**
     * @param userId   int
     * @param topicId  int
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
     * @param userId    int
     * @param topicsIds Collection<Integer>
     * @param pageable  Pageable
     * @return интервью определённой категории, в которых пользователь НЕ участвует
     */
    @Query("SELECT i FROM interview i WHERE i.id NOT IN"
            + " (SELECT w.interview.id FROM wisher w WHERE w.userId = :userId)"
            + " AND i.topicId IN :topicsIds")
    Page<Interview> findInterviewByUserIdNotAndByTopicIdIn(
            @Param("userId") int userId,
            @Param("topicsIds") Collection<Integer> topicsIds,
            Pageable pageable);

    /**
     * Возвращает все собеседования на который пользователь должен оставить отзыв.
     * nativeQuery = true;
     * Описание построения запроса:
     * Через внутренние объединения получаем список всех собеседований,
     * которые присутствуют в таблице wisher с признаком approve=true,
     * а также которые принадлежат указанному пользователю.
     * Конечная выборка получает все ID собеседований которых нет в таблице cd_feedback.
     * Ожидаемое поведение: пользователь не владелец собеседования,
     * но он одобренный участник и не оставил отзыв, метод вернет список с ID этого собеседования.
     * Пользователь является автором собеседования и он одобрил участника и не оставил отзыв,
     * метод вернет список с ID этого собеседования.
     * Так же если пользователь уже оставил отзыв на собеседование с ID то это собеседование не попадает в выборку.
     *
     * @param userId ID User
     * @return List<Interview>
     */
    @Query(value = """
            SELECT DISTINCT i.*
            FROM interview i
                     JOIN wisher w ON i.id = w.interview_id AND w.approve AND (i.submitter_id = :userId OR w.user_id = :userId)
            WHERE NOT EXISTS(SELECT 1
                             FROM cd_feedback cf
                             WHERE cf.interview_id = i.id
                               AND cf.user_id = :userId)
            """, nativeQuery = true)
    List<Interview> findAllByUserIdWisherIsApproveAndNoFeedback(@Param("userId") int userId);

    /**
     * Получаем из базы ТРИ последние интервью отсортированные по убыванию по дате их создания.
     *
     * @return LIST из ТРЕХ последних интервью
     */
    @Query(value = "SELECT i.* FROM interview i WHERE i.status = :status ORDER BY i.create_date DESC LIMIT 3", nativeQuery = true)
    List<Interview> findLastInterviews(@Param("status") StatusInterview status);

    /**
     * Получаем из базы новые интервью по статусу.
     *
     * @return LIST из новых интервью
     */
    @Query(value = "SELECT i.* FROM interview i WHERE i.status = :status", nativeQuery = true)
    List<Interview> findNewInterviews(@Param("status") StatusInterview status);

}
