package ru.job4j.interview.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.interview.domain.TAlgo;
import ru.job4j.interview.domain.Task;
import ru.job4j.interview.repository.TAlgoRepository;
import ru.job4j.interview.repository.TaskRepository;

import java.util.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class TAlgoService {

    private final TAlgoRepository tasks;

    @Autowired
    public TAlgoService(final TAlgoRepository tasks) {
        this.tasks = tasks;
    }

    public TAlgo findOne(int taskId) {
        return this.tasks.findById(taskId).get();
    }

    public TAlgo save(TAlgo task) {
        return this.tasks.save(task);
    }

    public List<TAlgo> findAll() {
        return Lists.newArrayList(this.tasks.findAll());
    }

    public List<TAlgo> findByPublished(boolean published) {

        return this.tasks.findByPublished(true);
    }
}
