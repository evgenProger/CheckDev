package ru.job4j.exam.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.exam.domain.ExamUser;
import ru.job4j.exam.repository.ExamUserRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "test" })
public class AnswerServiceTest {
    @Autowired
    private ExamUserService users;

    @Autowired
    private ExamUserRepository repository;

    @Ignore
    @Test()
    public void whenExamFinishThenCalcResult() {
        this.users.getWrongAnswer(this.repository.findById(8).get());
    }
}