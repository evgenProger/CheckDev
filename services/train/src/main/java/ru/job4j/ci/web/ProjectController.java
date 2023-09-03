package ru.job4j.ci.web;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.ci.domain.Project;
import ru.job4j.ci.domain.ProjectTask;
import ru.job4j.ci.service.JobService;
import ru.job4j.ci.service.ProjectService;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Comparator;

import static java.lang.Integer.compare;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projects;

    private final JobService jobs;

    @Autowired
    public ProjectController(ProjectService projects, JobService jobs) {
        this.projects = projects;
        this.jobs = jobs;
    }

    @GetMapping("/")
    String projects(Model model) {
        model.addAttribute("projects", this.projects.getAll());
        return "projects";
    }

    @GetMapping("/{projectId}")
    String project(@PathVariable int projectId, Model model) {
        model.addAttribute("project", this.projects.findOne(projectId));
        return "project/info";
    }

    @GetMapping("/run/{projectId}")
    String run(@PathVariable int projectId) throws ParseException, SchedulerException {
        this.projects.run(projectId);
        return "redirect:/projects/";
    }

    @RequestMapping(value = "/task/log/{taskId}", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    String log(@PathVariable int taskId, HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return this.projects.getProjectTask(taskId).getLog();
    }


    @GetMapping("/update")
    String update(@RequestParam(required = false) Integer id, Model model) {
        Project project = new Project();
        if (id != null) {
            project = this.projects.findOne(id);
        }
        model.addAttribute("project", project);
        model.addAttribute("jobs", this.jobs.getAll());
        return "project/update";
    }

    @GetMapping("/delete")
    String delete(@RequestParam Integer id) {
        this.projects.delete(id);
        return "redirect:/projects/";
    }

    @PostMapping("/save")
    String save(@ModelAttribute Project project, Model model) throws ParseException, SchedulerException {
        project.setStatus(Project.Status.STOP);
        this.projects.save(project);
        return "redirect:/projects/";
    }
}
