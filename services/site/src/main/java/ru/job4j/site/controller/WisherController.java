package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.enums.StatusInterview;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.WisherDto;
import ru.job4j.site.dto.WisherNotifiDTO;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.NotificationService;
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
    private final InterviewService interviewService;

    private final NotificationService notificationService;

    /**
     * Подать заявку на участие в собеседовании.
     *
     * @param wisherNotifiDTO WisherNotifiDTO
     * @param request   HttpServletRequest
     * @return String page.
     */
    @PostMapping("/create")
    public String createWisher(@ModelAttribute WisherNotifiDTO wisherNotifiDTO,
                               HttpServletRequest request) {
        var token = RequestResponseTools.getToken(request);
        int interviewId = wisherNotifiDTO.getInterviewId();
        int userId = wisherNotifiDTO.getUserId();
        var contactBy = wisherNotifiDTO.getContactBy();
        var wisherDto = new WisherDto(0, interviewId, userId, contactBy, false);
        wisherService.saveWisherDto(token, wisherDto);
        notificationService.sendParticipateAuthor(token, wisherNotifiDTO);
        return "redirect:/interview/" + interviewId;
    }

    /**
     * Одобрить участника собеседование, а остальных участников отклонить
     *
     * @param param   Map<String, String>
     * @param request HttpServletRequest
     * @return String page.
     */
    @PostMapping("/dismissed")
    public String dismissedWisher(@RequestParam Map<String, String> param,
                                  HttpServletRequest request) throws JsonProcessingException {
        var token = RequestResponseTools.getToken(request);
        var interviewId = param.get("interviewId");
        var wisherId = param.get("wisherId");
        var wisherUserId = param.get("wisherUserId");
        wisherService.setNewApproveByWisherInterview(
                token, interviewId, wisherId, true);
        InterviewDTO interview = interviewService.getById(token, Integer.parseInt(interviewId));
        interview.setAgreedWisherId(Integer.parseInt(wisherUserId));
        interviewService.update(token, interview);
        interviewService.updateStatus(token, Integer.parseInt(interviewId), StatusInterview.IN_PROGRESS.getId());
        return "redirect:/interview/" + interviewId;
    }
}
