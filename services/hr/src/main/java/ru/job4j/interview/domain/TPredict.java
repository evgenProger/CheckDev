package ru.job4j.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**

 * create table task_predict (
 id serial not null primary key,
 task_id int not null references task(id),
 key int not null,
 value text
 );
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "task_predict")
public class TPredict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="task_id", updatable = false)
    private Task task;

    private Key key;

    private String value;

    public TPredict createClone(Task clone) {
        return new TPredict(clone, this.key, this.value);
    }

    public enum Key {
        EQ, NOT_EQ, CONTAINS, NOT_CONTAINS, GREAT, LESS
    }

    public TPredict() {
    }

    public TPredict(Task task, Key key, String value) {
        this.task = task;
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPredict tPredict = (TPredict) o;

        return id == tPredict.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
