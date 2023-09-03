package ru.job4j.exam.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.exam.domain.QOpt;
import ru.job4j.exam.service.QOptService;
import ru.job4j.exam.service.QuestionService;

import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RestController
@RequestMapping("/options")
public class QOptController {
    private final QOptService options;

    private final QuestionService questions;

    @Autowired
    public QOptController(final QOptService options, final QuestionService questions) {
        this.options = options;
        this.questions = questions;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/{questionId}")
    public List<QOpt> options(@PathVariable int questionId) {
        return this.options.findByQuestionId(questionId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/find/{id}")
    public QOpt option(@PathVariable int id) {
        return this.options.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @PostMapping("/")
    public QOpt create(@RequestBody QOpt option) {
        return this.options.save(option);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/")
    public boolean delete(@RequestParam int id) {
        return this.options.delete(id);
    }
}
