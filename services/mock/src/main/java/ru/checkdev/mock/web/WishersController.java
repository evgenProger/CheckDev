package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;
import ru.checkdev.mock.service.InterviewService;
import ru.checkdev.mock.service.WisherService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/wishers")
@AllArgsConstructor
public class WishersController {

    private final WisherService wisherService;

    private final InterviewService interviewService;

    @GetMapping("/")
    public ResponseEntity<List<Wisher>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Wisher>> findByInterview(@Valid @PathVariable int id) throws SQLException {
        Optional<Interview> interviewOptional = interviewService.findById(id);
        if (interviewOptional.isEmpty()) {
            throw new SQLException("This interview is missing");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findByInterview(interviewOptional.get()));
    }

    @GetMapping("/dto/")
    public ResponseEntity<List<WisherDto>> findAllWisherDto() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findAllWisherDto());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<List<WisherDto>> findDtoByInterview(@Valid @PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findWisherByInterviewId(id));
    }
}
