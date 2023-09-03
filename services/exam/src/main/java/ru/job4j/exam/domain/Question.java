package ru.job4j.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 *
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private int pos;

    private String hint;

    @ManyToOne
    @JoinColumn(name="exam_id", updatable = false)
    private Exam exam;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE)
    @JoinColumn(name="question_id", updatable = false)
    private List<QOpt> options;

    public Question() {
    }

    public Question(int id) {
        this.id = id;
    }

    public List<QOpt> getOptions() {
        return options;
    }

    public void setOptions(List<QOpt> options) {
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
