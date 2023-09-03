package ru.job4j.exam.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.exam.domain.*;
import ru.job4j.exam.service.AnswerService;
import ru.job4j.exam.service.ExamService;
import ru.job4j.exam.service.QOptService;
import ru.job4j.exam.service.QuestionService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final ExamService exams;

    private final QuestionService questions;

    private final AnswerService answers;

    private final QOptService options;

    @Autowired
    public QuestionController(final ExamService exams, final QuestionService questions,
                              AnswerService answers, QOptService options) {
        this.exams = exams;
        this.questions = questions;
        this.answers = answers;
        this.options = options;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/")
    public List<Question> questions() {
        return this.questions.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/find/{questionId}")
    public ResponseEntity<HashMap<String, Object>> find(@PathVariable int questionId) {
        Question question = this.questions.findById(questionId);
        List<Question> qus = this.questions.findByExamId(question.getExam().getId());
        ListIterator<Question> it = qus.listIterator();
        HashMap<String, Object> result = new HashMap<>();
        while (it.hasNext()) {
            Question current = it.next();
            if (current.equals(question)) {
                result.put("current", current);
                if (it.hasNext()) {
                    result.put("next", it.next());
                    it.previous();
                }
                if (it.hasPrevious()) {
                    Question previous = it.previous();
                    if (previous.equals(current) && it.hasPrevious()) {
                        previous = it.previous();
                    } else {
                        previous = null;
                    }
                    result.put("previous", previous);
                }
                break;
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @GetMapping("/{examId}")
    public List<Question> exam(@PathVariable int examId) {
        return this.questions.findByExamId(examId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @PostMapping("/")
    public Question create(@RequestBody Question question) {
        return this.questions.save(question);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/")
    public boolean delete(@RequestParam int id) {
        return this.questions.delete(id);
    }
}
