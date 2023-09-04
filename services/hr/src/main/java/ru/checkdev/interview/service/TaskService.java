package ru.checkdev.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.interview.domain.*;
import ru.checkdev.interview.repository.*;

import java.util.*;
import java.util.stream.Collectors;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class TaskService {

    private final TaskRepository tasks;

    private final TValueRepository values;

    private final TPredictRepository predicts;

    private final ITaskRepository itasks;

    private final IValueRepository ivalues;

    @Autowired
    public TaskService(final TaskRepository tasks, final TValueRepository values, TPredictRepository predicts,
                       final ITaskRepository itasks, final IValueRepository ivalues) {
        this.tasks = tasks;
        this.values = values;
        this.predicts = predicts;
        this.itasks = itasks;
        this.ivalues = ivalues;
    }

    public Task findOne(int taskId) {
        return this.tasks.findById(taskId).get();
    }

    @Transactional
    public Task create(Task task) {
        List<Task> tasks = this.tasks.findByVacancyIdByPosAsc(task.getVacancy().getId());
        if (tasks.isEmpty()) {
            task.setPos(0);
        } else {
            task.setPos(tasks.get(tasks.size() - 1).getPos() + 1);
        }
        return this.update(task);
    }

    @Transactional
    public Task update(Task task) {
        this.tasks.save(task);
        this.updateTaskValue(task);
        this.updateTaskFilter(task);
        return task;
    }

    private void updateTaskFilter(final Task task) {
        this.predicts.deleteByTask(task.getId());
        List<TPredict> filters = task.getFilters().stream()
                .filter(f -> f.getKey() != null && f.getValue() != null)
                .collect(Collectors.toList());
        filters.forEach(
                f -> {
                    f.setTask(task);
                    f.setId(0);
                    predicts.save(f);
                }
        );
    }

    private void updateTaskValue(final Task task) {
        List<TValue> tvalues = task.getValues().stream()
                .filter(v -> v.getValue() != null)
                .collect(Collectors.toList());
        tvalues.forEach(
                v -> {
                    v.setTask(task);
                    values.save(v);
                }
        );
        if (!tvalues.isEmpty()) {
            values.deleteNotIn(
                    task.getValues().stream().map(
                            TValue::getId
                    ).collect(Collectors.toList()),
                    task.getId()
            );
        }
    }

    public List<Task> findByVacancyId(int vacancyId) {
        return this.tasks.findByVacancyIdByPosAsc(vacancyId);
    }

    /**
     * Delete task.
     * @param taskId task id.
     * @return true if deleted is possible.
     */
    @Transactional
    public boolean delete(int taskId) {
        this.ivalues.deleteByTask(taskId);
        this.itasks.deleteByTask(taskId);
        this.predicts.deleteByTask(taskId);
        this.values.deleteByTask(taskId);
        this.tasks.deleteById(taskId);
        return true;
    }

    @Transactional
    public void changePosition(int currentId, int nextId, String key) {
        Task current = this.tasks.findById(currentId).get();
        Task next = this.tasks.findById(nextId).get();
        int position = current.getPos();
        this.tasks.updatePosition(currentId, next.getPos());
        this.tasks.updatePosition(nextId, position);
    }

    public IValue findIValue(int ivalueId) {
        return this.ivalues.findById(ivalueId).get();
    }
}
