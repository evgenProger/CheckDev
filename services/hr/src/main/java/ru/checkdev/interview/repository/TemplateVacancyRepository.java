package ru.checkdev.interview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.interview.domain.TemplateVacancy;

public interface TemplateVacancyRepository extends CrudRepository<TemplateVacancy, Integer> {
}
