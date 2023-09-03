package ru.job4j.interview.domain;

import javax.persistence.*;
import java.util.Calendar;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="interview_id", updatable = false)
    private Interview interview;

    @ManyToOne
    @JoinColumn(name="task_id", updatable = false)
    private Task task;

    private boolean passed;

    private int position;

    @Enumerated(EnumType.ORDINAL)
    private Key key = Key.INIT;

    private Calendar created;

    public Track() {
    }

    public Track(Interview interview, Task task, boolean passed, int position, Calendar created) {
        this(0, interview, task, passed, position, created);
    }

    public Track(int id, Interview interview, Task task, boolean passed, int position, Calendar created) {
        this.id = id;
        this.interview = interview;
        this.task = task;
        this.passed = passed;
        this.position = position;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        return id == track.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Key {
        INIT, PASSED, SUCCESS, FAILURE
    }
}
