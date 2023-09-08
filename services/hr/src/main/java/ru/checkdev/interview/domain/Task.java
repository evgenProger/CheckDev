package ru.checkdev.interview.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private int pos;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne
    @JoinColumn(name="vacancy_id", updatable = false)
    private Vacancy vacancy;

    @OneToMany
    @JoinColumn(name="task_id", updatable = false)
    private List<TValue> values = Collections.EMPTY_LIST;

    @OneToMany
    @JoinColumn(name="task_id", updatable = false)
    private List<TPredict> filters = Collections.EMPTY_LIST;

    public Task() {
    }

    public Task(int id, String name, String description, int pos, Type type, Vacancy vacancy, List<TValue> values, List<TPredict> filters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pos = pos;
        this.type = type;
        this.vacancy = vacancy;
        this.values = values;
        this.filters = filters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public List<TValue> getValues() {
        return values;
    }

    public void setValues(List<TValue> values) {
        this.values = values;
    }

    public List<TPredict> getFilters() {
        return filters;
    }

    public void setFilters(List<TPredict> filters) {
        this.filters = filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public Task createClone(Vacancy vacancy) {
        return new Task(this.name, this.description, this.pos, this.type, vacancy);
    }

    public Task(String name, String description, int pos, Type type, Vacancy vacancy) {
        this.name = name;
        this.description = description;
        this.pos = pos;
        this.type = type;
        this.vacancy = vacancy;
    }

    public enum Type {
        SINGLE_LINE, AREA, LIST,
        MULTILIST, INT, DATE, ATTACH,
        TASK, TEST, URL
    }
}
