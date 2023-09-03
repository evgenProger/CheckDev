package ru.job4j.ci.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.ci.domain.Project;
import ru.job4j.ci.domain.ProjectTask;
import ru.job4j.ci.domain.Task;
import ru.job4j.ci.repository.ProjectRepository;
import ru.job4j.ci.repository.ProjectTaskRepository;
import ru.job4j.ci.repository.TaskRepository;

import java.util.Calendar;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository tasks;
    private final ProjectRepository projects;
    private final ProjectTaskRepository ptasks;

    @Autowired
    public TaskServiceImpl(TaskRepository tasks, ProjectRepository projects, ProjectTaskRepository ptasks) {
        this.tasks = tasks;
        this.projects = projects;
        this.ptasks = ptasks;
    }

    @Override
    public List<Task> getAll() {
        return Lists.newArrayList(this.tasks.findAll());
    }

    @Transactional
    @Override
    public Task save(Task task) {
        this.tasks.save(task);
        for (Project project : this.projects.findAllByJobs(task.getJob())) {
            if (!project.getTasks().stream()
                    .anyMatch(it -> it.getTask().equals(task))) {
                this.ptasks.save(new ProjectTask(
                        task,
                        project,
                        Calendar.getInstance(),
                        ProjectTask.Result.NEW
                ));
            }

        }
        return task;
    }

    @Override
    public Task findOne(int taskId) {
        return this.tasks.findById(taskId).get();
    }

    @Override
    public void delete(int taskId) {
        this.ptasks.removeByTask(new Task(taskId, null, null));
        this.tasks.deleteById(taskId);
    }
}
