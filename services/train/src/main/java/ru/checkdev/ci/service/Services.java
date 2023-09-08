package ru.checkdev.ci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class Services {
    private final ProjectService projects;

    private final JobService jobs;

    private final TaskService service;

    @Autowired
    public Services(ProjectService projects, JobService jobs, TaskService service) {
        this.projects = projects;
        this.jobs = jobs;
        this.service = service;
    }

    public ProjectService getProjects() {
        return projects;
    }

    public JobService getJobs() {
        return jobs;
    }

    public TaskService getService() {
        return service;
    }
}
