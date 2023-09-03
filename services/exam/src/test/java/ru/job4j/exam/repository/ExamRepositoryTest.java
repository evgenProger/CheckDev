package ru.job4j.exam.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.exam.service.ExamService;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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
public class ExamRepositoryTest {
    @Autowired
    private ExamRepository exams;

    @Autowired
    private ExamUserRepository users;

    @Autowired
    private QOptRepository qopts;

    @Autowired
    private AOptRepository aopts;

    @Autowired
    private ExamService examService;


    @Test
    public void createExam() {
        this.users.getMax(5);
    }
}