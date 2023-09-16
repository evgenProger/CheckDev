package ru.job4j.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping({"/", "index"})
    public String getIndexPage(@RequestParam(value = "error", required = false) String error,
                               Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Email or Password is incorrect !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "index";
    }
}
