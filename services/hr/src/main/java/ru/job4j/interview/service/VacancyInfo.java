package ru.job4j.interview.service;

import ru.job4j.interview.domain.Permission;
import ru.job4j.interview.domain.Vacancy;

import java.util.Objects;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class VacancyInfo {
    private final Permission permission;
    private final Vacancy vacancy;

    public VacancyInfo(Permission permission, Vacancy vacancy) {
        this.permission = permission;
        this.vacancy = vacancy;
    }

    public Permission getPermission() {
        return permission;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacancyInfo that = (VacancyInfo) o;
        return Objects.equals(vacancy, that.vacancy);
    }

    @Override
    public int hashCode() {

        return Objects.hash(vacancy);
    }
}
