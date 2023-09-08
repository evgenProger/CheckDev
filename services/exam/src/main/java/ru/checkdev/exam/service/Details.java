package ru.checkdev.exam.service;

import org.springframework.data.util.Pair;
import ru.checkdev.exam.domain.ExamUser;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Details {
    private final long total;
    private final long average;
    private final List<Pair<ExamUser, ExamUser.Person>> persons;

    public Details(long total, long average, List<Pair<ExamUser, ExamUser.Person>> persons) {
        this.total = total;
        this.average = average;
        this.persons = persons;
    }

    public long getTotal() {
        return total;
    }

    public long getAverage() {
        return average;
    }

    public List<Pair<ExamUser, ExamUser.Person>> getPersons() {
        return persons;
    }
}
