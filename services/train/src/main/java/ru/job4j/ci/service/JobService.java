package ru.job4j.ci.service;

import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Project;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface JobService {
    List<Job> getAll();

    Job save(Job job);

    void delete(int id);

    Job findOne(int id);
}
