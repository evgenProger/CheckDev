package ru.checkdev.interview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.checkdev.interview.domain.Vacancy;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface VacancyRepository extends PagingAndSortingRepository<Vacancy, Integer> {

    List<Vacancy> findByActiveOrderByPublishedDesc(boolean active);

    List<Vacancy> findByKeyAndArchive(String key, boolean archive, Pageable pageable);

    List<Vacancy> findByIdIn(List<Integer> collect);

    @Query("select new ru.checkdev.interview.domain.Vacancy(v.id, v.name, v.published, v.company, v.location, v.rate, v.experience) "
            + "from vacancy as v  where LOWER(v.name) LIKE %?1% OR LOWER(v.description) LIKE %?1% and v.active is ?2")
    List<Vacancy> findByNameContainingIgnoreCaseAndActive(String name, boolean active, Pageable page);

    @Query("select count(v.id) from vacancy as v where v.key = ?1 and v.active is true")
    Long countAllVacancyByPersonKey(String key);

    @Query("select count(v.id) from vacancy as v where v.archive = ?1")
    Long countAllByArchive(boolean archive);

    Page<Vacancy> findByArchive(boolean archive, Pageable pageable);

    @Query("select count(v.id) from vacancy as v where v.active is true")
    Long countVacancyByActiveTrue();

    @Query("select count(v.id) from vacancy as v where lower(v.name) LIKE %?1% or LOWER(v.description) Like %?1% and v.active is true")
    Long countVacancyByActiveTrueAndName(String name);

    @Query("select new ru.checkdev.interview.domain.Vacancy(v.id, v.name, v.published, v.company, v.location, v.rate, v.experience) from vacancy as v "
            + "where v.active is ?1 order by v.published desc")
    List<Vacancy> findByActive(boolean active, Pageable pageable);
}
