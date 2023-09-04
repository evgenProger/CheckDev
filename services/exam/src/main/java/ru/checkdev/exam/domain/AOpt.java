package ru.checkdev.exam.domain;

import javax.persistence.*;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "answer_opt")
public class AOpt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "answer_id", updatable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "question_opt_id", updatable = false)
    private QOpt opt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public QOpt getOpt() {
        return opt;
    }

    public void setOpt(QOpt opt) {
        this.opt = opt;
    }
}
