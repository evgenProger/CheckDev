package ru.job4j.ci.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.ci.domain.Job;
import ru.job4j.ci.domain.Setting;
import ru.job4j.ci.service.JobService;
import ru.job4j.ci.service.SettingService;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@Controller
@RequestMapping("/settings")
public class SettingController {

    private final SettingService settings;

    @Autowired
    public SettingController(SettingService settings) {
        this.settings = settings;
    }

    @GetMapping("/")
    String projects(Model model){
        model.addAttribute("settings", this.settings.getAll());
        return "settings";
    }

    @GetMapping("/update")
    String update(@RequestParam(required = false) Integer id, Model model){
        model.addAttribute("setting", id != null ? this.settings.findOne(id) : new Setting());
        model.addAttribute("types", Setting.Type.values());
        return "setting";
    }

    @GetMapping("/delete")
    String delete(@RequestParam Integer id) {
        this.settings.delete(id);
        return "redirect:/jobs/";
    }

    @PostMapping("/save")
    String save(@ModelAttribute Setting setting) {
        this.settings.save(setting);
        return "redirect:/settings/";
    }
}
