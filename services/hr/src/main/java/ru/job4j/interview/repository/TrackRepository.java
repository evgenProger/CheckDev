package ru.job4j.interview.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.Track;

import java.util.Collection;
import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TrackRepository extends CrudRepository<Track, Integer> {
    List<Track> findByInterviewIdOrderByPositionAsc(int interviewId);

    @Query("select count(t.id) from track as t "
            + "where t.passed = ?1 and t.interview.id = ?2")
    Long countPassedTaskByInterviewId(boolean passed, int interviewId);

    @Query("select count(t.id) from track as t where t.interview.id = ?1")
    Long countAllTaskByInterviewId(int interviewId);

    @Modifying
    @Query("update track t set t.passed = ?2, t.key = ?3 where t.id = ?1")
    int update(int id, boolean passed, Track.Key result);

    List<Track> findByInterviewIdIn(Collection<Integer> ids);

    @Query("select t from track as t where t.interview.id = ?1 and t.task.id = ?2 and t.task is null")
    Track findByInterviewIdAndTaskId(int interviewId, int stageId);

    @Modifying
    @Transactional
    @Query("delete from track t where t.interview.id = ?1")
    void deleteByInterviewId(int id);
}

