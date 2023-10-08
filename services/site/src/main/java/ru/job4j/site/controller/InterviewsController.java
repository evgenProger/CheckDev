package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.service.InterviewsService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interviews")
@AllArgsConstructor
public class InterviewsController {

    private final InterviewsService interviewsService;

    @GetMapping("/")
    public String getAllInterviews(Model model, HttpServletRequest req) throws JsonProcessingException {
        var token = getToken(req);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Собеседования", "/interviews/"
        );
        model.addAttribute("interviews", interviewsService.getAll(token));
        model.addAttribute("statuses", StatusInterview.values());
        model.addAttribute("current_page", "interviews");
        return "interviews";
    }
}
