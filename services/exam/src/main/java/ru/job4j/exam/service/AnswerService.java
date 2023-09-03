package ru.job4j.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.*;
import ru.job4j.exam.repository.AOptRepository;
import ru.job4j.exam.repository.AnswerRepository;
import ru.job4j.exam.repository.ExamUserRepository;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class AnswerService {
    private final AnswerRepository answers;

    private final AOptRepository options;

    private final ExamUserRepository users;

    @Autowired
    public AnswerService(final AnswerRepository answers, final AOptRepository options, ExamUserRepository users) {
        this.answers = answers;
        this.options = options;
        this.users = users;
    }

    @Transactional
    public Answer save(ExamUser user, Question question, String[] options) {
        Answer answer = this.answers.findByUserIdAndQuestionId(user.getId(), question.getId());
        if (answer == null) {
            answer = new Answer();
            answer.setUser(user);
            answer.setQuestion(question);
            this.answers.save(answer);
        }
        this.options.deleteByAnswerId(answer.getId());
        for (String opt : options) {
            if (opt != null && !opt.isEmpty()) {
                AOpt o = new AOpt();
                o.setAnswer(answer);
                o.setOpt(new QOpt(Integer.valueOf(opt)));
                this.options.save(o);
            }
        }
        return answer;
    }

    public Answer findByUserIdAndQuestionId(int userId, int questionId) {
        return this.answers.findByUserIdAndQuestionId(userId, questionId);
    }
}
