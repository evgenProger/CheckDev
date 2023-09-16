package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.TopicsService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/topics")
@AllArgsConstructor
public class TopicsControl {
    private final TopicsService topicsService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public String getByCategory(@PathVariable int id, Model model, HttpServletRequest req) throws JsonProcessingException {
        var token = (String) req.getSession().getAttribute("token");
        model.addAttribute("categoryId", id);
        model.addAttribute("topics", topicsService.getByCategory(id, token));
        var userInfo = authService.userInfo(token);
        model.addAttribute("userInfo", userInfo);
        var canManage = userInfo.getRoles().stream()
                .anyMatch(role -> role.getValue().equals("ROLE_ADMIN"));
        model.addAttribute("canManage", canManage);
        return "topic/topics";
    }
}
