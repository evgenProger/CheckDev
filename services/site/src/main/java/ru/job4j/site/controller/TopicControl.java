package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.TopicDTO;
import ru.job4j.site.dto.TopicLiteDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.TopicsService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicControl {
    private final TopicsService topicsService;
    private final AuthService authService;

    @GetMapping("/createForm/{categoryId}")
    public String createForm(@PathVariable int categoryId, Model model, HttpServletRequest req)
            throws JsonProcessingException {
        var token = getToken(req);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            model.addAttribute("userInfo", token);
            RequestResponseTools.addAttrCanManage(model, userInfo);
        }
        model.addAttribute("categoryId", categoryId);
        return "topic/createForm";
    }

    @PostMapping("/create")
    public String createTopic(@ModelAttribute TopicLiteDTO topic, HttpServletRequest req)
            throws JsonProcessingException {
        topicsService.create(getToken(req), topic);
        return "redirect:/categories/";
    }

    @GetMapping("/updateForm")
    public String updateForm(@ModelAttribute(name = "id") int id, Model model, HttpServletRequest req)
            throws JsonProcessingException {
        var token = getToken(req);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            var topic = topicsService.getById(id, token);
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("topic", topic);
            RequestResponseTools.addAttrCanManage(model, userInfo);
        }
        return "topic/updateForm";
    }

    @PostMapping("/update")
    public String updateTopic(TopicDTO topic, HttpServletRequest req) throws JsonProcessingException {
        topicsService.update(getToken(req), topic);
        return "redirect:/categories/";
    }

    @PostMapping("/delete")
    public String deleteTopic(@ModelAttribute(name = "id") int id, HttpServletRequest req)
            throws JsonProcessingException {
        topicsService.delete(getToken(req), id);
        return "redirect:/categories/";
    }
}
