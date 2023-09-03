package ru.job4j.exam.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.job4j.exam.domain.Question;
import ru.job4j.exam.repository.QuestionRepository;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class QuestionService {
    private final QuestionRepository questions;

    @Autowired
    public QuestionService(final QuestionRepository questions) {
        this.questions = questions;
    }

    public Question save(Question question) {
        return this.questions.save(question);
    }

    public List<Question> findAll() {
        return Lists.newArrayList(this.questions.findAll());
    }

    public boolean delete(int id) {
        this.questions.deleteById(id);
        return true;
    }

    public Question findById(int id) {
        return this.questions.findById(id).get();
    }

    public List<Question> findByExamId(int examId) {
        return this.questions.findByExamIdOrderByPosAsc(examId);
    }

    public List<Question> findByExamId(int examId, PageRequest page) {
        return this.questions.findByExamId(examId, page);
    }

    public Long countByExamId(int examId) {
        return this.questions.countByExamId(examId);
    }
}
