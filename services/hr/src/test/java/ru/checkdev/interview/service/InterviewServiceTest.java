package ru.checkdev.interview.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.interview.domain.Interview;
import ru.checkdev.interview.domain.Vacancy;

import java.util.Calendar;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class InterviewServiceTest {
    @Autowired
    private InterviewService service;

    @Test
    public void whenRegDuplicatePersonThenResultEmpty() {
        Interview interview = new Interview();
        interview.setVacancy(new Vacancy(1));
        interview.setKey("test");
        interview.setStart(Calendar.getInstance());
        service.start(interview);
    }

}