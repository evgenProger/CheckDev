package ru.job4j.site.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.job4j.site.dto.FilterDTO;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.service.InterviewsService;

import java.util.List;

@Component
public class InterviewsRequestManager {

    private final InterviewsService interviewsService;

    public InterviewsRequestManager(InterviewsService interviewsService) {
        this.interviewsService = interviewsService;
    }

    public Page<InterviewDTO> find(String token, FilterDTO filter,
                                   int page, int size) throws JsonProcessingException {
        Page<InterviewDTO> result;
        var userId = filter.getUserId();
        var topicId = filter.getTopicId();
        var filterProfileId = filter.getFilterProfile();
        if (filterProfileId > 0) {
            result = switch (filterProfileId) {
                case 1 -> topicId > 0 ? interviewsService
                        .getByTopicIdAndSubmitterId(topicId, userId, false, page, size)
                        : interviewsService.getBySubmitterId(userId, false, page, size);
                case 2 -> topicId > 0 ? interviewsService
                        .getByTopicIdAndUserIdAsWisher(topicId, userId, false, page, size)
                        : interviewsService.getByUserIdAsWisher(userId, false, page, size);
                case 3 -> topicId > 0 ? interviewsService
                        .getByTopicIdAndSubmitterId(topicId, userId, true, page, size)
                        : interviewsService.getBySubmitterId(userId, true, page, size);
                case 4 -> topicId > 0 ? interviewsService
                        .getByTopicIdAndUserIdAsWisher(topicId, userId, true, page, size)
                        : interviewsService.getByUserIdAsWisher(userId, true, page, size);
                default -> topicId > 0 ? interviewsService.getByTopicId(topicId, page, size)
                        : interviewsService.getAll(token, page, size);
            };
        } else {
            result = topicId > 0 ? interviewsService.getByTopicId(topicId, page, size)
                    : interviewsService.getAll(token, page, size);
        }
        return result;
    }

    public Page<InterviewDTO> findByTopicsList(List<Integer> topicIds,
                                   FilterDTO filter, int page, int size) throws JsonProcessingException {
        Page<InterviewDTO> result;
        var userId = filter.getUserId();
        var filterProfileId = filter.getFilterProfile();
        if (filterProfileId > 0) {
            result = switch (filterProfileId) {
                case 1 -> interviewsService
                        .getByTopicsIdsAndSubmitterId(topicIds, userId, false, page, size);
                case 2 ->  interviewsService
                        .getByTopicsIdsAndUserIdAsWisher(topicIds, userId, false, page, size);
                case 3 -> interviewsService
                        .getByTopicsIdsAndSubmitterId(topicIds, userId, true, page, size);
                case 4 -> interviewsService
                        .getByTopicsIdsAndUserIdAsWisher(topicIds, userId, true, page, size);
                default -> interviewsService.getByTopicsIds(topicIds, page, size);
            };
        } else {
            result = interviewsService.getByTopicsIds(topicIds, page, size);
        }
        return result;
    }
}
