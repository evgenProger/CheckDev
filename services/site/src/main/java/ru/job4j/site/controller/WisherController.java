package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.site.dto.WisherDto;
import ru.job4j.site.service.WisherService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Контроллер обработки запросов на подписку на собеседование.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.10.2023
 */
@Controller
@RequestMapping("/wisher")
@AllArgsConstructor
public class WisherController {
    private final WisherService wisherService;

    @PostMapping("/create")
    public String createWisher(@RequestParam Map<String, String> wishParam,
                               HttpServletRequest request) {
        var token = RequestResponseTools.getToken(request);
        int interviewId = Integer.parseInt(wishParam.get("interviewId"));
        int userId = Integer.parseInt(wishParam.get("userId"));
        var contactBy = wishParam.get("email");
        var wisherDto = new WisherDto(0, interviewId, userId, contactBy, false);
        wisherService.saveWisherDto(token, wisherDto);
        return "redirect:/interview/" + interviewId;
    }
}
