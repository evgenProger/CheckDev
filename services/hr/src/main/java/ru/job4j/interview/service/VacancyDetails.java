package ru.job4j.interview.service;

import ru.job4j.interview.domain.Vacancy;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class VacancyDetails {
    private final Vacancy vacancy;
    private final Statistic statistic;

    public VacancyDetails(Vacancy vacancy, Statistic statistic) {
        this.vacancy = vacancy;
        this.statistic = statistic;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public static final class Statistic {
        private final long total;
        private final long success;
        private final long reject;
        private final long fail;
        private final long hire;
        private InterviewDetail.Person person;


        public Statistic(final long total, final long success,
                         final long fail, final long reject, final long hire, final InterviewDetail.Person person) {
            this.total = total;
            this.success = success;
            this.fail = fail;
            this.reject = reject;
            this.hire = hire;
            this.person = person;
        }


        public Statistic(final long total, final long success,
                         final long fail, final long reject, final long hire) {
            this.total = total;
            this.success = success;
            this.fail = fail;
            this.reject = reject;
            this.hire = hire;
        }

        public long getTotal() {
            return total;
        }

        public long getSuccess() {
            return success;
        }

        public long getReject() {
            return reject;
        }

        public long getFail() {
            return fail;
        }

        public long getHire() {
            return hire;
        }

        public InterviewDetail.Person getPerson() {
            return person;
        }

        public void setPerson(InterviewDetail.Person person) {
            this.person = person;
        }
    }

}
