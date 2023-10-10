package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/interviews")
@AllArgsConstructor
public class InterviewsController {

    private final InterviewService interviewService;

    /*Аннотация не работает
    @PreAuthorize("isAuthenticated()") */
    @GetMapping("/")
    public ResponseEntity<List<Interview>> findAll() throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findAll());
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<Interview>> findByType(@PathVariable int type) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findByType(type));
    }
}
