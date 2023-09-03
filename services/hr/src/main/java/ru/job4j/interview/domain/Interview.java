package ru.job4j.interview.domain;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "interview")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * User ID, who passes interview.
     */
    private String key;

    private Calendar start;

    private Calendar finish;

    @Enumerated(EnumType.ORDINAL)
    private Result result = Result.PROCESS;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", updatable = false)
    private Vacancy vacancy;

    public Interview() {
    }

    public Interview(int id, String key, Calendar start, Calendar finish, Result result, Vacancy vacancy) {
        this.id = id;
        this.key = key;
        this.start = start;
        this.finish = finish;
        this.result = result;
        this.vacancy = vacancy;
    }

    public Interview(Result result, Vacancy vacancy, String key) {
        this.result = result;
        this.vacancy = vacancy;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getFinish() {
        return finish;
    }

    public void setFinish(Calendar finish) {
        this.finish = finish;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interview interview = (Interview) o;

        return id == interview.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Result {
        PROCESS, SUCCESS, FAIL, REJECT, OFFER, HIRE
    }
}
