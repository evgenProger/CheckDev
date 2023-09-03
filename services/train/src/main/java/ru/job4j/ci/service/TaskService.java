package ru.job4j.ci.service;

import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Task;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TaskService {
    List<Task> getAll();

    Task save(Task task);

    Task findOne(int taskId);

    void delete(int taskId);
}
