package ru.checkdev.mock.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "InterviewsController", description = "Interviews REST API")
@RestController
@RequestMapping("/interviews")
@AllArgsConstructor
public class InterviewsController {

    private final InterviewService interviewService;

    /*Аннотация не работает
    @PreAuthorize("isAuthenticated()") */
    @GetMapping("/")
    public ResponseEntity<Page<Interview>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findPaging(page, size));
    }

    @GetMapping("/last")
    public ResponseEntity<List<Interview>> findLastThree() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findLast());
    }

    @GetMapping("/{mode}")
    public ResponseEntity<List<Interview>> findByMode(@PathVariable int mode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findByMode(mode));
    }

    @GetMapping("/findByTopicId/{topicId}")
    public ResponseEntity<Page<Interview>> findByTopicId(
            @PathVariable int topicId,
            @RequestParam(required = false, defaultValue = "0") int userId,
            @RequestParam(required = false, defaultValue = "0") int submitterId,
            @RequestParam(required = false, defaultValue = "false") boolean not,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        Page<Interview> result;
        if (userId > 0) {
            result = !not
                    ? interviewService.findByUserIdAsWisherByTopic(userId, topicId, page, size)
                    : interviewService.findByUserIdAsNotWisherByTopic(userId, topicId, page, size);
        } else if (submitterId > 0) {
            result = !not
                    ? interviewService.findByTopicIdAndSubmitterId(topicId, submitterId, page, size)
                    : interviewService.findByTopicIdAndSubmitterIdNot(topicId, submitterId, page, size);
        } else {
            result = interviewService.findByTopicId(topicId, page, size);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/findByTopicsIds/{tids}")
    public ResponseEntity<Page<Interview>> findByCategory(
            @PathVariable String tids,
            @RequestParam(required = false, defaultValue = "0") int userId,
            @RequestParam(required = false, defaultValue = "0") int submitterId,
            @RequestParam(required = false, defaultValue = "false") boolean not,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        var topicIdsArr = tids.split(",");
        List<Integer> topicIds = new ArrayList<>();
        for (String id : topicIdsArr) {
            if (id.matches("^\\d+$")) {
                topicIds.add(Integer.valueOf(id));
            }
        }
        Page<Interview> result;
        if (userId > 0) {
            result = !not
                    ? interviewService.findByUserIdAsWisherByTopicList(userId, topicIds, page, size)
                    : interviewService.findByUserIdAsNotWisherByTopicList(userId, topicIds, page, size);
        } else if (submitterId > 0) {
            result = !not
                    ? interviewService.findByTopicsListIdAndSubmitterId(topicIds, submitterId, page, size)
                    : interviewService.findByTopicsListIdAndSubmitterIdNot(topicIds, submitterId, page, size);
        } else {
            result = interviewService.findByTopicsIds(topicIds, page, size);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/findBySubmitter/{submitterId}")
    public ResponseEntity<Page<Interview>> findBySubmitter(
            @PathVariable int submitterId,
            @RequestParam(required = false, defaultValue = "false") boolean not,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(!not ? interviewService.findBySubmitterId(submitterId, page, size)
                        : interviewService.findBySubmitterIdNot(submitterId, page, size));
    }

    @GetMapping("/findByWisher/{userId}")
    public ResponseEntity<Page<Interview>> findByWisher(
            @PathVariable int userId,
            @RequestParam(required = false, defaultValue = "false") boolean not,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(!not ? interviewService.findByUserIdAsWisher(userId, page, size)
                        : interviewService.findByUserIdAsNotWisher(userId, page, size));
    }

    @GetMapping("/findByUserIdRelated/{userId}")
    public ResponseEntity<Page<Interview>> findByUserIdRelated(
            @PathVariable int userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findPagingByUserIdRelated(page, size, userId));
    }

    @GetMapping("/noFeedback/{uId}")
    public ResponseEntity<List<Interview>> getAllNoFeedback(@PathVariable("uId") int uId) {
        List<Interview> interviews = interviewService.findAllIdByNoFeedback(uId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(interviews);
    }
}
