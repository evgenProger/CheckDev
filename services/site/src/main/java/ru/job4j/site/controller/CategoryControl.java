package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.CategoriesService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryControl {
    private final CategoriesService categoriesService;
    private final AuthService authService;

    @GetMapping("/createForm")
    public String createForm(Model model, HttpServletRequest req) throws JsonProcessingException {
        setManage(model, req);
        return "createForm";
    }

    @PostMapping("/")
    public String createCategory(@ModelAttribute CategoryDTO category, HttpServletRequest req) throws JsonProcessingException {
        var token = (String) req.getSession().getAttribute("token");
        categoriesService.create(token, category);
        return "redirect:/categories/";
    }

    @GetMapping("/editForm/{id}/{name}")
    public String editForm(Model model,
                           @PathVariable("id") int id,
                           @PathVariable("name") String name,
                           HttpServletRequest req) throws JsonProcessingException {
        model.addAttribute("category", new CategoryDTO(id, name));
        setManage(model, req);
        return "editCategoryForm";
    }

    @PostMapping("/update")
    public String updateCategory(Model model,
                                 @ModelAttribute CategoryDTO category,
                                 HttpServletRequest req) throws JsonProcessingException {
        var token = (String) req.getSession().getAttribute("token");
        categoriesService.update(token, category);
        setManage(model, req);
        return "redirect:/categories/";
    }

    private void setManage(Model model, HttpServletRequest req) throws JsonProcessingException {
        var token = (String) req.getSession().getAttribute("token");
        var userInfo = authService.userInfo(token);
        model.addAttribute("userInfo",userInfo);
        var canManage = userInfo.getRoles().stream()
                .anyMatch(role -> role.getValue().equals("ROLE_ADMIN"));
        model.addAttribute("canManage", canManage);
    }
}
