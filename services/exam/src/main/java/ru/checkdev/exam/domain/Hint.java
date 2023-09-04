package ru.checkdev.exam.domain;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Hint {
    private String question;
    private String hint;

    public Hint(String question, String hint) {
        this.question = question;
        this.hint = hint;
    }

    public String getQuestion() {
        return question;
    }

    public String getHint() {
        return hint;
    }
}
