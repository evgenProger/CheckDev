package ru.job4j.ci.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Task;
import ru.job4j.ci.service.JobService;
import ru.job4j.ci.service.TaskService;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService tasks;

    @Autowired
    public TaskController(TaskService tasks) {
        this.tasks = tasks;
    }

    @GetMapping("/add/{jobId}")
    String add(@PathVariable int jobId, @RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("task", id != null ? this.tasks.findOne(id) : new Task());
        model.addAttribute("types", Task.Type.values());
        model.addAttribute("jobId", jobId);
        return "task";
    }

    @GetMapping("/delete")
    String delete(@RequestParam Integer id) {
        this.tasks.delete(id);
        return "redirect:/jobs/";
    }

    @PostMapping("/save")
    String save(@ModelAttribute Task task) {
        this.tasks.save(task);
        return "redirect:/jobs/";
    }
}
