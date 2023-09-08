package ru.checkdev.exam.domain;

import javax.persistence.*;

/**
 * create table question_opt (
 id serial not null primary key,
 description text,
 question_id int not null references question(id)
 );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "question_opt")
public class QOpt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @ManyToOne
    @JoinColumn(name="question_id", updatable = false)
    private Question question;

    private boolean correct;

    private int pos;

    public QOpt() {
    }

    public QOpt(int id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QOpt qOpt = (QOpt) o;

        return id == qOpt.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
