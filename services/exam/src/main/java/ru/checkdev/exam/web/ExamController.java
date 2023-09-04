package ru.checkdev.exam.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.exam.domain.Exam;
import ru.checkdev.exam.service.ExamService;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RestController
@RequestMapping("/exams")
public class ExamController {
    private final ExamService exams;
    private final String ping = "{}";

    @Autowired
    public ExamController(final ExamService exams) {
        this.exams = exams;
    }

    @GetMapping("/ping")
    public String ping() {
        return this.ping;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/")
    public List<Exam> exams() {
        return this.exams.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/{id}")
    public Exam exam(@PathVariable int id) {
        return this.exams.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @PostMapping("/")
    public Exam create(@RequestBody Exam exam) {
        return this.exams.save(exam);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/")
    public boolean delete(@RequestParam int id) {
        return this.exams.delete(id);
    }
}
