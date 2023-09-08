package ru.checkdev.ci.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.ci.domain.Job;
import ru.checkdev.ci.service.JobService;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobs;

    @Autowired
    public JobController(JobService jobs) {
        this.jobs = jobs;
    }

    @GetMapping("/")
    String projects(Model model){
        model.addAttribute("jobs", this.jobs.getAll());
        return "jobs";
    }

    @GetMapping("/update")
    String update(@RequestParam(required = false) Integer id, Model model){
        model.addAttribute("job", id != null ? this.jobs.findOne(id) : new Job());
        return "job";
    }

    @GetMapping("/delete")
    String delete(@RequestParam Integer id) {
        this.jobs.delete(id);
        return "redirect:/jobs/";
    }

    @PostMapping("/save")
    String save(@ModelAttribute Job job) {
        this.jobs.save(job);
        return "redirect:/jobs/";
    }
}
