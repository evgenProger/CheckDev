package ru.job4j.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * create table interview_value (
 * id serial not null primary key,
 * interview_task_id int not null references interview_task(id),
 * key varchar(2000),
 * value text,
 * attach bytea
 * );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "interview_value")
public class IValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "interview_task_id", updatable = false)
    private ITask task;

    @Enumerated(EnumType.ORDINAL)
    private Key key = Key.SINGLE;

    private String value;

    @JsonIgnore
    private byte[] attach;

    public IValue() {
    }

    public IValue(ITask task, Key key, String value, byte[] attach) {
        this.task = task;
        this.key = key;
        this.value = value;
        this.attach = attach;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ITask getTask() {
        return task;
    }

    public void setTask(ITask task) {
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

    public byte[] getAttach() {
        return attach;
    }

    public void setAttach(byte[] attach) {
        this.attach = attach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IValue iValue = (IValue) o;

        return id == iValue.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Key {
        SINGLE
    }
}
