package ru.job4j.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * create table interview_task (
 * id serial not null primary key,
 * interview_id int not null references interview(id),
 * task_id int not null references task(id),
 * start timestamp not null,
 * finish timestamp
 * )
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "interview_task")
public class ITask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "interview_id", updatable = false)
    private Interview interview;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "task_id", updatable = false)
    private Task task;

    private Calendar start;

    private Calendar finish;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "interview_task_id", updatable = false)
    private List<IValue> values = new ArrayList<>();

    public ITask() {
    }

    public ITask(Interview interview, Task task, Calendar start, Calendar finish, List<IValue> values) {
        this.interview = interview;
        this.task = task;
        this.start = start;
        this.finish = finish;
        this.values = values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    @JsonIgnore
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public List<IValue> getValues() {
        return values;
    }

    public void setValues(List<IValue> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ITask iTask = (ITask) o;

        return id == iTask.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
