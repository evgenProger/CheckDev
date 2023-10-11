package ru.checkdev.mock.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

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
    @Query("SELECT new ru.checkdev.mock.dto.WisherDto(w.id, w.interview.id, w.userId, w.contactBy, w.approve) FROM wisher w WHERE w.interview.id =:interviewId")
    List<WisherDto> findWisherDTOByInterviewId(@Param("interviewId") int interviewId);

    /**
     * Метод нативным запросом формирует список всех участников собеседований
     *
     * @return List<WisherDTO>
     */
    @Query("SELECT new ru.checkdev.mock.dto.WisherDto(w.id, w.interview.id, w.userId, w.contactBy, w.approve) FROM wisher w")
    List<WisherDto> findAllWiserDto();
}
