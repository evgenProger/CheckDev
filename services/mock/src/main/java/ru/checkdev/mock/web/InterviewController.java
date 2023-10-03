package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/interview")
@AllArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    /* Аннотация не работает
    @PreAuthorize("isAuthenticated()")*/
    @PostMapping("/")
    public ResponseEntity<Interview> save(@Valid @RequestBody Interview interview) throws SQLException {
        Optional<Interview> rsl = interviewService.save(interview);
        if (rsl.isEmpty()) {
            throw new SQLException("An error occurred while saving data");
        }
        return new ResponseEntity<Interview>(
                rsl.orElse(new Interview()),
                rsl.isPresent() ? HttpStatus.CREATED : HttpStatus.CONFLICT
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@Valid @PathVariable int id) throws SQLException {
        Optional<Interview> rsl = interviewService.findById(id);
        if (rsl.isEmpty()) {
            throw new SQLException("There is no interview with this number");
        }
        return new ResponseEntity<Interview>(
                rsl.orElse(new Interview()),
                rsl.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @PutMapping("/")
    public ResponseEntity<Interview> update(@Valid @RequestBody Interview interview) {
        return new ResponseEntity<Interview>(interview,
                interviewService.update(interview) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Interview> delete(@Valid @PathVariable int id) {
        Interview interview = new Interview();
        interview.setId(id);
        return new ResponseEntity<Interview>(interview,
                interviewService.delete(interview) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }
}
