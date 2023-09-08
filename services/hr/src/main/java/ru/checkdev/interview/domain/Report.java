package ru.checkdev.interview.domain;

import javax.persistence.*;

/**

 * create table report (
 id serial not null primary key,
 vacancy_id int not null references vacancy(id),
 name varchar(2000),
 type int not null,
 period int not null,
 sorted_id int not null references task(id),
 sorted_by int not null
 );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="vacancy_id", updatable = false)
    private Vacancy vacancy;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Enumerated(EnumType.ORDINAL)
    private Period period;

    @ManyToOne
    @JoinColumn(name="task_id", updatable = false)
    private Task sorted;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "sorted_by")
    private SortBy by;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Task getSorted() {
        return sorted;
    }

    public void setSorted(Task sorted) {
        this.sorted = sorted;
    }

    public SortBy getBy() {
        return by;
    }

    public void setBy(SortBy by) {
        this.by = by;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;

        return id == report.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Type {
        IMMEDIATELY, PERIOD
    }

    public enum Period {
        HOUR, DAY, DAY_2, DAY_3, WEEK
    }

    public enum SortBy {
        ASC, DESC
    }
}
