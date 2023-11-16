package ru.checkdev.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

import java.util.Collection;
import java.util.List;

public interface WisherRepository extends CrudRepository<Wisher, Integer> {

    List<Wisher> findByInterview(Interview interview);

    List<Wisher> findAll();

    /**
     * Метод нативным запросом формирует список всех участников собеседований,
     * возвращая список DTO моделей WisherDTO
     *
     * @param interviewId int Interview ID
     * @return List<WisherDTO>
     */
    @Query("SELECT new ru.checkdev.mock.dto.WisherDto(w.id, w.interview.id, w.userId, w.contactBy, w.approve, w.status) FROM wisher w WHERE w.interview.id =:interviewId")
    List<WisherDto> findWisherDTOByInterviewId(@Param("interviewId") int interviewId);

    /**
     * Метод нативным запросом формирует список всех участников собеседований
     *
     * @return List<WisherDTO>
     */
    @Query("SELECT new ru.checkdev.mock.dto.WisherDto(w.id, w.interview.id, w.userId, w.contactBy, w.approve, w.status) FROM wisher w")
    List<WisherDto> findAllWiserDto();

    /**
     * Метод устанавливает одобренному участнику approved = true.
     *
     * @param interviewId ID interview
     * @param wisherId    ID wisher
     * @param newStatusId ID new Status
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE wisher w SET w.approve = true, w.status=:newStatusId WHERE w.interview.id=:interviewId AND w.id=:wisherId ")
    void setWisherStatus(@Param("interviewId") int interviewId,
                         @Param("wisherId") int wisherId,
                         @Param("newStatusId") int newStatusId);

    /**
     * Метод устанавливает всем участникам approved = false которых wisher.id != ID не равны.
     *
     * @param interviewId ID interview
     * @param notWisherId ID Wisher exclude
     * @param newStatusId ID new Status
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE wisher w SET w.approve = false, w.status=:newStatusId WHERE w.interview.id=:interviewId AND w.id!=:notWisherId")
    void setNotWisherStatus(@Param("interviewId") int interviewId,
                            @Param("notWisherId") int notWisherId,
                            @Param("newStatusId") int newStatusId);

    /**
     * @param userId int
     * @param pageable Pageable
     * @return интервью, в которых пользователь участвует
     */
    @Query("SELECT i FROM wisher w JOIN w.interview i WHERE w.userId = :userId")
    Page<Interview> findInterviewByUserId(@Param("userId") int userId, Pageable pageable);

    /**
     * @param userId int
     * @param pageable Pageable
     * @return интервью, в которых пользователь участвует и одобрен к участию
     */
    @Query("SELECT i FROM wisher w JOIN w.interview i WHERE w.userId = :userId AND w.approve IS TRUE")
    Page<Interview> findInterviewByUserIdApproved(@Param("userId") int userId, Pageable pageable);

    /**
     * @param userId int
     * @param topicId int
     * @param pageable Pageable
     * @return интервью определённой темы, в которых пользователь участвует
     */
    @Query("SELECT i FROM wisher w "
            + "JOIN w.interview i "
            + "WHERE w.userId = :userId"
            + " AND i.topicId = :topicId")
    Page<Interview> findInterviewByUserIdAndByTopicId(@Param("userId") int userId,
                                                      @Param("topicId") int topicId,
                                                      Pageable pageable);

    /**
     * @param userId int
     * @param topicsIds Collection<Integer>
     * @param pageable Pageable
     * @return интервью определённой категории, в которых пользователь участвует
     */
    @Query("SELECT i FROM wisher w "
            + "JOIN w.interview i "
            + "WHERE w.userId = :userId "
            + "AND i.topicId IN :topicsIds")
    Page<Interview> findInterviewByUserIdAndByTopicIdIn(
            @Param("userId") int userId,
            @Param("topicsIds") Collection<Integer> topicsIds,
            Pageable pageable);
}
