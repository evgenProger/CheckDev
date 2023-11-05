package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.component.InterviewsRequestManager;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.FilterDTO;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.dto.TopicIdNameDTO;
import ru.job4j.site.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/interviews")
@Slf4j
@AllArgsConstructor
public class InterviewsController {

    private final InterviewsService interviewsService;
    private final ProfilesService profilesService;
    private final CategoriesService categoriesService;
    private final TopicsService topicsService;
    private final AuthService authService;
    private final FilterService filterService;
    private final WisherService wisherService;
    private final InterviewsRequestManager interviewsRequestManager;
    private final NotificationService notifications;

    @GetMapping("/")
    public String getAllInterviews(Model model,
                                   HttpServletRequest req,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size,
                                   HttpSession session) {
        try {
            var token = getToken(req);
            var user = authService.userInfo(token);
            var userId = user != null ? user.getId() : 0;
            var filter = userId > 0
                    ? filterService.getByUserId(token, userId)
                    : (FilterDTO) session.getAttribute("filter");
            var isFiltered = filter != null
                    && (filter.getCategoryId() > 0
                    || filter.getFilterProfile() > 0);
            Page<InterviewDTO> interviewsPage;
            List<TopicIdNameDTO> topicIdNameDTOS = new ArrayList<>();
            var categoryName = "";
            var topicName = "";
            var filterProfileName = "";
            var filterProfiles = filterService.getProfiles();
            var categories = categoriesService.getAll();
            if (isFiltered) {
                var categoryId = filter.getCategoryId();
                var topicId = filter.getTopicId();
                var filterProfileId = filter.getFilterProfile();
                if (categoryId > 0) {
                    topicIdNameDTOS = topicsService.getTopicIdNameDtoByCategory(categoryId);
                }
                interviewsPage = categoryId <= 0 || topicId > 0
                        ? interviewsRequestManager.find(token, filter, page, size)
                        : interviewsRequestManager.findByTopicsList(
                        topicIdNameDTOS.stream().map(TopicIdNameDTO::getId).toList(), filter, page, size);
                categoryName = categoriesService.getNameById(categories, categoryId);
                topicName = topicId > 0 ? topicsService.getNameById(topicId) : "";
                filterProfileName = filterProfileId > 0
                        ? filterService.getNameById(filterProfiles, filterProfileId) : "";
            } else {
                interviewsPage = interviewsService.getAll(token, page, size);
            }
            Set<ProfileDTO> userList = interviewsPage.toList().stream()
                    .map(x -> profilesService.getProfileById(x.getSubmitterId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            var wishers = wisherService.getAllWisherDtoByInterviewId(token, "");
            var interviewStatistic = wisherService.getInterviewStatistic(wishers);
            interviewsService.setCountWishers(interviewsPage, token, page, size);
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Собеседования", String.format("/interviews/?page=%d&?size=%d", page, size)
            );
            model.addAttribute("statisticMap", interviewStatistic);
            model.addAttribute("interviewsPage", interviewsPage);
            model.addAttribute("statuses", StatusInterview.values());
            model.addAttribute("current_page", "interviews");
            model.addAttribute("users", userList);
            model.addAttribute("categories", categories);
            model.addAttribute("filter", filter);
            model.addAttribute("userId", userId);
            model.addAttribute("categoryName", categoryName);
            model.addAttribute("topicName", topicName);
            model.addAttribute("topics", topicIdNameDTOS);
            model.addAttribute("filterProfiles", filterProfiles);
            model.addAttribute("filterProfileName", filterProfileName);
            if (token != null) {
                model.addAttribute("botMessages",
                        notifications.findBotMessageByUserId(token, user.getId()));
            }
        } catch (Exception e) {
            RequestResponseTools.addAttrBreadcrumbs(model,
                    "Главная", "/index",
                    "Собеседования", String.format("/interviews/?page=%d&?size=%d", page, size)
            );
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
        }
        return "interview/interviews";
    }

    @PostMapping("/reload")
    @ResponseBody
    public void reload(@RequestBody FilterDTO filter,
                             Model model,
                             HttpServletRequest req,
                             @RequestParam(required = false, defaultValue = "0") int page,
                             @RequestParam(required = false, defaultValue = "20") int size,
                             HttpSession session) {
        session.setAttribute("filter", filter);
        getAllInterviews(model, req, page, size, session);
    }
}
