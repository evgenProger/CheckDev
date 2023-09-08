package ru.checkdev.ci.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String url;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "project_job",
            joinColumns = {
                    @JoinColumn(
                            name = "project_id", nullable = false, updatable = false
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "job_id", nullable = false, updatable = false
                    )
            }
    )
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name="project_id", updatable = false)
    private List<ProjectTask> tasks;

    public Project() {
    }

    public Project(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ProjectTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ProjectTask> tasks) {
        this.tasks = tasks;
    }


    public List<ProjectTask> getSortedTasks() {
        Collections.sort(this.tasks);
        return this.tasks;
    }

    public ProjectTask.Result getBuild() {
        return this.tasks.stream().anyMatch(
                it -> it.getResult() == ProjectTask.Result.FAIL
        ) ? ProjectTask.Result.FAIL : ProjectTask.Result.SUCCESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return id == project.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Status {
        STOP, RUN, SUSPECT
    }
}
