package ru.checkdev.ci.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Calendar;

import static java.lang.Integer.compare;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "project_task")
public class ProjectTask implements Comparable<ProjectTask> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name="task_id")
    private Task task;

    private String log;

    public Calendar time;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @Enumerated(EnumType.ORDINAL)
    private Result result;

    private String value;

    public ProjectTask() {
    }

    public ProjectTask(Task task, Project project, Calendar time, Result result) {
        this.task = task;
        this.time = time;
        this.project = project;
        this.result = result;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
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

        ProjectTask that = (ProjectTask) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(ProjectTask that) {
        return compare(this.task.getPos(), that.task.getPos());
    }

    public enum Result {
        NEW, SUCCESS, FAIL
    }
}
