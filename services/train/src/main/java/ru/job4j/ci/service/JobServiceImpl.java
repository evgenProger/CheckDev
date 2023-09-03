package ru.job4j.ci.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Project;
import ru.job4j.ci.repository.JobRepository;
import ru.job4j.ci.repository.ProjectRepository;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobs;

    @Autowired
    public JobServiceImpl(JobRepository jobs) {
        this.jobs = jobs;
    }

    @Override
    public List<Job> getAll() {
        return Lists.newArrayList(this.jobs.findAll());
    }

    @Override
    public Job save(Job job) {
        this.jobs.save(job);
        return job;
    }

    @Override
    public void delete(int id) {
        this.jobs.deleteById(id);
    }

    @Override
    public Job findOne(int id) {
        return this.jobs.findById(id).get();
    }
}
