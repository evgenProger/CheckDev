package ru.checkdev.interview.service;


import ru.checkdev.interview.domain.Interview;
import ru.checkdev.interview.domain.Vacancy;

/**
 * @author Hincu Andrei (andreih1981@gmail.com)on 07.09.2018.
 * @version $Id$.
 * @since 0.1.
 */
public class VacancyForCandidate {

   private  Vacancy vacancy;

   private Interview.Result result;

    public VacancyForCandidate() {
    }

    public VacancyForCandidate(final Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public VacancyForCandidate(final Vacancy vacancy, final Interview.Result result) {
        this.vacancy = vacancy;
        this.result = result;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public Interview.Result getResult() {
        return result;
    }

    public void setResult(Interview.Result result) {
        this.result = result;
    }
}
