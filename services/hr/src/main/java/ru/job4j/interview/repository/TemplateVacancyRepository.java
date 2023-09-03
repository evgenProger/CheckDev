package ru.job4j.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.interview.domain.TemplateVacancy;

public interface TemplateVacancyRepository extends CrudRepository<TemplateVacancy, Integer> {
}
