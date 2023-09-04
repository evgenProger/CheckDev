package ru.checkdev.interview.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.interview.domain.Interview;

import java.util.Calendar;
import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface InterviewRepository extends CrudRepository<Interview, Integer> {
    Interview findByVacancyIdAndKey(int vacancyId, String key);

    List<Interview> findByVacancyId(int vacancyId, Pageable pageable);

    List<Interview> findByVacancyIdAndResult(int vacancyId, Interview.Result result, Pageable pageable);

    @Query("select count(i.id) from interview as i where i.vacancy.id = ?1")
    Long countAllInterviewByVacancyId(int vacancyId);

    @Query("select count(i.id) from interview as i where i.vacancy.id = ?1 and i.result = ?2")
    Long getStatistic(int vacancyId, Interview.Result success);

    List<Interview> findByKey(final String key);

    List<Interview> findByFinishBetween(Calendar start, Calendar finish);


    @Modifying
    @Transactional
    @Query("update interview i set i.result = ?2, i.finish = ?3 where i.id = ?1")
    void updateRsl(int id, Interview.Result rsl, Calendar finish);

    @Modifying
    @Transactional
    @Query("delete from interview i where i.id = ?1")
    void deleteByInterviewId(int id);
}
