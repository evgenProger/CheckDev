package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.service.CategoriesService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoriesControl {
    private final CategoriesService categoriesService;
    @GetMapping("/")
    public String categories(Model model, HttpServletRequest req) throws JsonProcessingException {
        var token = (String) req.getSession().getAttribute("token");
        model.addAttribute("categories", categoriesService.getAll(token));
        return "categories";
    }
}
