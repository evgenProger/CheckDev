package ru.job4j.interview.domain;

import javax.persistence.*;

/**

 * create table report_field (
 id serial not null primary key,
 field_id int not null references task(id),
 position int not null
 );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "report_field")
public class RField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="task_id", updatable = false)
    private Task task;

    private int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RField rField = (RField) o;

        return id == rField.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
