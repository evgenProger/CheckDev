package ru.job4j.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.*;
import ru.job4j.interview.repository.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class VacancyService {

    private final VacancyRepository vacancies;
    private final PermissionRepository permissions;
    private final TeamRepository teams;
    private final TaskRepository tasks;
    private final TPredictRepository predictRepository;
    private final TValueRepository tValueRepository;

    @Autowired
    public VacancyService(VacancyRepository vacancies, PermissionRepository permissions,
                          TeamRepository teams, TaskRepository tasks,
                          TPredictRepository tPredictRepository, TValueRepository tValueRepository) {
        this.vacancies = vacancies;
        this.permissions = permissions;
        this.teams = teams;
        this.tasks = tasks;
        this.predictRepository = tPredictRepository;
        this.tValueRepository = tValueRepository;
    }

    /**
     * Все активные вакансии
     *
     * @return лист
     */
    public List<Vacancy> findByActive(Pageable pageable) {
        return this.vacancies.findByActive(true, pageable);
    }

    public List<Vacancy> findByName(String name, Pageable page) {
        return this.vacancies.findByNameContainingIgnoreCaseAndActive(name.toLowerCase(), true, page);
    }

    public Long getCountByName(String name) {
        return this.vacancies.countVacancyByActiveTrueAndName(name.toLowerCase());
    }

    @Transactional
    public List<Vacancy> findByOwner(String key, boolean archive, Pageable pageable) {
        List<Permission> permissions = this.permissions.findByTeamIdIn(
                this.teams.findByMemberKey(key)
        );
        List<Vacancy> rls = this.vacancies.findByKeyAndArchive(key, archive, pageable);
        List<Integer> list = permissions.stream().map(p -> p.getVacancy().getId()).collect(Collectors.toList());
        rls.addAll(this.vacancies.findByIdIn(
                list
        ));
        rls.sort((o1, o2) -> -o1.getCreated().compareTo(o2.getCreated()));
        return rls;
    }

    public Vacancy findById(int id) {
        return this.vacancies.findById(id).get();
    }

    /**
     * Возвращает сохраненную вакансию или бросает Exception, если
     * вакансия не прошла валидацию.
     * 1. Если вакансии в базе нет -> устанавливаем время создания и сохраняем
     * (данный вариант работает при создании новой вакансии);
     * 2. Если вакансия существует и пользователя пытается ее опубликовать (isActive = true)
     * проверяем наличие хотябы одного этапа (stage) и в каждом этапе хотябы одного задания (task);
     * 3. Если мы останавливаем опубликованную ваканси (isActive = false) и данная вакансия ранее
     * была опубликована -> убираем дату опубликования.
     *
     * @param vacancy - вакансия
     * @return - вакансию, сохраненную в базе
     */

    @Transactional
    public Vacancy save(Vacancy vacancy) {
        Vacancy current = this.vacancies.findById(vacancy.getId()).get();
        if (current == null) {
            vacancy.setCreated(Calendar.getInstance());
        } else if (vacancy.isActive()) {
            vacancy.setPublished(Calendar.getInstance());
        } else if (!vacancy.isActive() && vacancy.getPublished() != null) {
            vacancy.setPublished(null);
        }
        return this.vacancies.save(vacancy);
    }

    public boolean hasAccess(int vacancyId, String person) {
        List<Permission> permissions = this.permissions.findByTeamIdIn(
                this.teams.findByMemberKey(person)
        );
        return permissions.stream().anyMatch(p -> p.getVacancy().getId() == vacancyId);
    }

    @Transactional
    public void copy(int vacancyId) {
        Vacancy vacDb = this.vacancies.findById(vacancyId).get();
        Vacancy vacCopy = this.vacancies.save(vacDb.createClone());
    }

    public Page<Vacancy> findAll(Pageable pageable) {
        return this.vacancies.findByArchive(false, pageable);
    }

    public Long getCountOfVacancyBuPersonKey(String key) {
        return this.vacancies.countAllVacancyByPersonKey(key);
    }

    public Long getCountOfAllVacancy() {
        return this.vacancies.countAllByArchive(false);
    }

    public Long getCountOfVacancyActive() {
        return this.vacancies.countVacancyByActiveTrue();
    }
}
