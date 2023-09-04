package ru.checkdev.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * create table answer (
 * id serial not null primary key,
 * question_id int not null references question(id),
 * question_opt_id int not null references question_opt(id)
 * );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "question_id", updatable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "exam_user_id", updatable = false)
    private ExamUser user;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "answer_id")
    private List<AOpt> options;

    public List<AOpt> getOptions() {
        return options;
    }

    public void setOptions(List<AOpt> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExamUser getUser() {
        return user;
    }

    public void setUser(ExamUser user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
