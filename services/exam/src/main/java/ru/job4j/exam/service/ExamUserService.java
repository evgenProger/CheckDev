package ru.job4j.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.*;
import ru.job4j.exam.repository.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class ExamUserService {
    private final ExamRepository exams;

    private final ExamUserRepository users;

    private final QuestionRepository questions;

    private final AnswerRepository answers;

    private final AOptRepository aopts;

    @Autowired
    public ExamUserService(final ExamRepository exams, final ExamUserRepository users,
                           final QuestionRepository questions, final AnswerRepository answers, AOptRepository aopts) {
        this.exams = exams;
        this.users = users;
        this.questions = questions;
        this.answers = answers;
        this.aopts = aopts;
    }

    @Transactional
    public ExamUser start(String key, int examId) {
        ExamUser user = this.users.findByKeyAndExamId(key, examId);
        if (user == null) {
            user = new ExamUser();
            user.setKey(key);
            user.setExam(new Exam(examId));
            user.setStart(Calendar.getInstance());
            this.users.save(user);
        }
        return user;
    }

    @Transactional
    public void cleanup(ExamUser user) {
        this.aopts.deleteByExamUserId(user.getId());
        user.setFinish(null);
        user.setResult(0);
        this.users.save(user);
    }

    public boolean delete(int id) {
        this.users.deleteById(id);
        return true;
    }

    public ExamUser findByKeyAndExamId(String key, int examId) {
        return this.users.findByKeyAndExamId(key, examId);
    }

    public List<ExamUser> findByKey(String key) {
        return this.users.findByKey(key);
    }

    @Transactional
    public List<Hint> getWrongAnswer(ExamUser user) {
        List<Hint> hints = new ArrayList<>();
        List<Question> questions = this.questions.findByExamIdOrderByPosAsc(user.getExam().getId());
        List<Answer> answers = this.answers.findByUserId(user.getId());
        for (Question question : questions) {
            boolean taken = false;
            for (Answer answer : answers) {
                if (question.getId() == answer.getQuestion().getId()) {
                    taken = true;
                    List<QOpt> choose = answer.getOptions().stream().map(AOpt::getOpt)
                            .collect(Collectors.toList());
                    List<QOpt> qcorrect = question.getOptions().stream()
                            .filter(QOpt::isCorrect).collect(Collectors.toList());
                    if (!qcorrect.containsAll(choose)) {
                        hints.add(new Hint(question.getName(), question.getHint()));
                    }
                }
            }
            if (!taken) {
                hints.add(new Hint(question.getName(), question.getHint()));
            }
        }
        return hints;
    }

    @Transactional
    public ExamUser finish(ExamUser user) {
        List<Question> questions = this.questions.findByExamIdOrderByPosAsc(user.getExam().getId());
        List<Answer> answers = this.answers.findByUserId(user.getId());
        float correct = 0f;
        for (Question question : questions) {
            for (Answer answer : answers) {
                if (question.getId() == answer.getQuestion().getId()) {
                    List<QOpt> choose = answer.getOptions().stream().map(AOpt::getOpt)
                            .collect(Collectors.toList());
                    List<QOpt> qcorrect = question.getOptions().stream()
                            .filter(QOpt::isCorrect).collect(Collectors.toList());
                    if (qcorrect.containsAll(choose)) {
                        correct++;
                    }
                }
            }
        }
        user.setFinish(Calendar.getInstance());
        user.setResult((int) (correct / (questions.size()) * 100));
        user.setTotal(user.getTotal() + 1);
        this.users.save(user);
        return user;
    }
}
