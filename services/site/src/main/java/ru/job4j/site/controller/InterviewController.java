package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.TopicDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.TopicsService;
import javax.servlet.http.HttpServletRequest;
import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interview")
@AllArgsConstructor
public class InterviewController {

    private final AuthService authService;

    private final TopicsService topicsService;

    private final InterviewService interviewService;

    @GetMapping("/createForm")
    public String createForm(@ModelAttribute(name = "topicId") int topicId, Model model, HttpServletRequest req)
            throws JsonProcessingException {
        var token = getToken(req);
        var topic = new TopicDTO();
        if (token != null) {
            var userInfo = authService.userInfo(token);
            model.addAttribute("userInfo", token);
            RequestResponseTools.addAttrCanManage(model, userInfo);
            topic = topicsService.getById(topicId);
        }
        String categoryName = topic.getCategory().getName();
        int categoryId = topic.getCategory().getId();
        model.addAttribute("category", topic.getCategory());
        model.addAttribute("topic", topic);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/",
                String.format("%s. Темы", categoryName),
                String.format("/topics/%s/%d", categoryName, categoryId),
                topic.getName(), String.format("/topic/%s/%d/%d", categoryName, categoryId, topicId));
        return "interview/createForm";
    }

    @PostMapping("/create")
    public String createInterview(@ModelAttribute InterviewDTO interviewDTO, HttpServletRequest req)
            throws JsonProcessingException {
        var token = getToken(req);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            interviewDTO.setSubmitterId(userInfo.getId());
        }
        interviewService.create(getToken(req), interviewDTO);
        return "redirect:/interviews/";
    }
}
