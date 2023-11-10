package ru.checkdev.mock.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;

import javax.validation.Valid;
import java.sql.SQLException;

@Tag(name = "InterviewController", description = "Interview REST API")
@RestController
@RequestMapping("/interview")
@AllArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Interview> save(@Valid @RequestBody Interview interview) throws SQLException {
        return new ResponseEntity<>(
                interviewService
                        .save(interview)
                        .orElseThrow(() -> new SQLException("An error occurred while saving data")),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@Valid @PathVariable int id) {
        return interviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Interview> update(@Valid @RequestBody Interview interview) {
        return new ResponseEntity<Interview>(interview,
                interviewService.update(interview) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/status/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> updateStatusInterview(@RequestParam String id, @RequestParam String newStatus) {
        var idInterview = Integer.parseInt(id);
        var status = Integer.parseInt(newStatus);
        var result = interviewService.updateStatus(idInterview, status);
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();
    }
}
