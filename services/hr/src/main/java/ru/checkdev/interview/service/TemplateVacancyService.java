package ru.checkdev.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.interview.domain.TemplateVacancy;
import ru.checkdev.interview.repository.TemplateVacancyRepository;

/**
 * Author : Pavel Ravvich.
 * Created : 12.08.17.
 */
@Service
public class TemplateVacancyService {

    private final TemplateVacancyRepository repository;

    @Autowired
    public TemplateVacancyService(final TemplateVacancyRepository repository) {
        this.repository = repository;
    }

    public TemplateVacancy getById(final int id) {
        return repository.findById(id).get();
    }

    public void saveTemplateVacancy(final TemplateVacancy template) {
        repository.save(template);
    }

    public boolean deleteById(final int id) {
        boolean result = false;
        if (repository.existsById(id)) {
            repository.deleteById(id);
            result = true;
        }
        return result;
    }

    public boolean update(final TemplateVacancy template) {
        boolean result = false;
        if (repository.existsById(template.getId())) {
            repository.deleteById(template.getId());
            repository.save(template);
            result = true;
        }
        return result;
    }
}
