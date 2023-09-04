package ru.checkdev.interview.service;

import ru.checkdev.interview.domain.Comment;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CommentInfo {
    private Comment comment;

    private InterviewDetail.Person person;

    public CommentInfo(Comment comment, InterviewDetail.Person person) {
        this.comment = comment;
        this.person = person;
    }

    public Comment getComment() {
        return comment;
    }

    public InterviewDetail.Person getPerson() {
        return person;
    }
}
