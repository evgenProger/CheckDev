package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.service.InterviewsService;
import ru.job4j.site.service.ProfilesService;
import ru.job4j.site.service.WisherService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interviews")
public class InterviewsController {

    private final InterviewsService interviewsService;

    private final ProfilesService profilesService;

    private final WisherService wisherService;

    private final String key;

    public InterviewsController(InterviewsService interviewsService,
                                ProfilesService profilesService,
                                WisherService wisherService,
                                @Value("${server.auth.access.key}") String key) {
        this.interviewsService = interviewsService;
        this.profilesService = profilesService;
        this.wisherService = wisherService;
        this.key = key;
    }

    @GetMapping("/")
    public String getAllInterviews(Model model, HttpServletRequest req) throws JsonProcessingException {
        var token = getToken(req);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Собеседования", "/interviews/"
        );
        List<InterviewDTO> interviewDTOList = interviewsService.getAll(token);
        Set<ProfileDTO> userList = interviewDTOList.stream()
                .map(x -> profilesService.getProfileById(x.getSubmitterId(), key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        var wishers = wisherService.getAllWisherDtoByInterviewId(token, "");
        var interviewStatistic = wisherService.getInterviewStatistic(wishers);
        model.addAttribute("statisticMap", interviewStatistic);
        model.addAttribute("interviews", interviewDTOList);
        model.addAttribute("statuses", StatusInterview.values());
        model.addAttribute("current_page", "interviews");
        model.addAttribute("users", userList);
        return "interviews";
    }
}
