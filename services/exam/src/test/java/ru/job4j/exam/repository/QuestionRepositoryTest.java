package ru.job4j.exam.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.exam.domain.Exam;
import ru.job4j.exam.domain.Question;

/**
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "test" })
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questions;

    @Autowired
    AOptRepository options;

    @Autowired
    AnswerRepository answers;

    @Test
    public void whenLoadQuestionThenAnswersLoadToo() {
        Question question = new Question();
        question.setExam(new Exam(13));
        this.questions.save(question);
        options.findAll();
    }
}