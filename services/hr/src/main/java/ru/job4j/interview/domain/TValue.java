package ru.job4j.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "task_value")
public class TValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String value;

    private String info;

    public TValue() {
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="task_id", updatable = false)
    private Task task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TValue tValue = (TValue) o;

        return id == tValue.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public TValue createClone(Task task) {
        return new TValue(this.value, this.info, task);
    }

    public TValue(String value, String info, Task task) {
        this.value = value;
        this.info = info;
        this.task = task;
    }

}
