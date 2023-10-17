package ru.checkdev.mock.repository;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Filter;
import ru.checkdev.mock.domain.Interview;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotBlank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

@DataJpaTest()
@RunWith(SpringRunner.class)
class InterviewRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    public void injectedComponentAreNotNull() {
        Assertions.assertNotNull(entityManager);
        Assertions.assertNotNull(interviewRepository);
    }

    @Test
    public void whenFindInterviewByIdThenReturnEmpty() {
        Optional<Interview> interview = interviewRepository.findById(-1);
        MatcherAssert.assertThat(interview, is(Optional.empty()));
    }

    @Test
    public void whenInterviewFindByType() {
        var interview = new Interview();
        interview.setTypeInterview(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        entityManager.createQuery("delete from interview").executeUpdate();
        entityManager.persist(interview);
        var interviews = interviewRepository.findByTypeInterview(1);
        Assertions.assertTrue(interviews.size() > 0);
        Assertions.assertEquals(interviews.get(0), interview);
    }

    @Test
    public void whenInterviewNotFoundByType() {
        entityManager.createQuery("delete from interview").executeUpdate();
        var interviews = interviewRepository.findByTypeInterview(1);
        Assertions.assertEquals(0, interviews.size());
    }

    @Test
    public void whenFindAllInterview() {
        entityManager.createQuery("delete from interview").executeUpdate();
        var listInterview = interviewRepository.findAll();
        MatcherAssert.assertThat(listInterview, is(Collections.emptyList()));
    }

    @Test
    public void whenInterviewFindByTopicId() {
        var interview = new Interview();
        interview.setTypeInterview(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        entityManager.createQuery("delete from interview").executeUpdate();
        entityManager.persist(interview);
        var interviews = interviewRepository.findByTopicId(1);
        Assertions.assertTrue(interviews.size() > 0);
        Assertions.assertEquals(interviews.get(0), interview);
    }

    @Test
    public void whenInterviewNotFoundByTopicId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        var interviews = interviewRepository.findByTopicId(1);
        Assertions.assertEquals(0, interviews.size());
    }
}