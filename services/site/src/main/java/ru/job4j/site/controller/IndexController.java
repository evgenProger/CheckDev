package ru.job4j.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping({"/", "index"})
    public String getIndexPage(Model model) {
        return "index";
    }
}
