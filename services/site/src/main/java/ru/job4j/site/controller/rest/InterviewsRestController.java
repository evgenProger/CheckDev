package ru.job4j.site.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.service.InterviewsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@RestController
@RequestMapping("/interviews_rest")
@AllArgsConstructor
public class InterviewsRestController {

    private final InterviewsService interviewsService;

    @GetMapping("/")
    public ResponseEntity<List<InterviewDTO>> getAll(HttpServletRequest req)
            throws JsonProcessingException {
        return new ResponseEntity<>(
                interviewsService.getAll(getToken(req)), HttpStatus.OK
        );
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<List<InterviewDTO>> getByFilter(@PathVariable int topicId)
            throws JsonProcessingException {
        return new ResponseEntity<>(
                interviewsService.getByTopicId(topicId), HttpStatus.OK
        );
    }
}
