package ru.checkdev.interview.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.interview.domain.TAlgo;
import ru.checkdev.interview.repository.TAlgoRepository;

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
