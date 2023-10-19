package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String getAllInterviews(Model model,
                                   HttpServletRequest req,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size)
            throws JsonProcessingException {
        var token = getToken(req);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Собеседования", String.format("/interviews/?page=%d&?size=%d", page, size)
        );
        Page<InterviewDTO> interviewsPage = interviewsService.getAll(token, page, size);
        Set<ProfileDTO> userList = interviewsPage.stream()
                .map(x -> profilesService.getProfileById(x.getSubmitterId(), key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        var wishers = wisherService.getAllWisherDtoByInterviewId(token, "");
        var interviewStatistic = wisherService.getInterviewStatistic(wishers);
        model.addAttribute("statisticMap", interviewStatistic);
        model.addAttribute("interviewsPage", interviewsPage);
        model.addAttribute("statuses", StatusInterview.values());
        model.addAttribute("current_page", "interviews");
        model.addAttribute("users", userList);
        return "interviews";
    }
}
