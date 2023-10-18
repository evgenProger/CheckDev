package ru.checkdev.mock.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@DataJpaTest()
@RunWith(SpringRunner.class)
class WisherRepositoryTest {
    private Interview interview;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WisherRepository wisherRepository;

    @BeforeEach
    public void clearTable() {
        interview = new Interview();
        interview.setTypeInterview(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("mail@mail");
        interview.setApproximateDate("approximate");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        entityManager.createQuery("delete from wisher").executeUpdate();
        entityManager.createQuery("delete from interview").executeUpdate();
        entityManager.persist(interview);
        entityManager.flush();
    }

    @Test
    public void injectedComponentAreNotNull() {
        assertNotNull(entityManager);
        assertNotNull(wisherRepository);
    }

    @Test
    public void whenFindInterviewByIdThenReturnEmpty() {
        Optional<Wisher> wisher = wisherRepository.findById(-1);
        assertThat(wisher, is(Optional.empty()));
    }

    @Test
    public void whenFindAllInterview() {
        var listWisher = wisherRepository.findAll();
        assertThat(listWisher, is(Collections.emptyList()));
    }

    @Test
    public void whenFindWisherByInterviewIdThenReturnListWisherDto() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false);
        entityManager.persist(wisher);
        entityManager.flush();
        var expect = List.of(
                new WisherDto(wisher.getId(), wisher.getInterview().getId(), wisher.getUserId(), wisher.getContactBy(), wisher.isApprove()));
        var actual = wisherRepository.findWisherDTOByInterviewId(interview.getId());
        assertThat(actual, is(expect));
    }

    @Test
    public void whenFindWisherByInterviewIdThenReturnEmptyList() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false);
        entityManager.persist(wisher);
        entityManager.flush();
        var actual = wisherRepository.findWisherDTOByInterviewId(-1);
        assertThat(actual, is(Collections.emptyList()));
    }

    @Test
    public void whenFindAllWisherDtoThenReturnListWisherDto() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", true);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.flush();
        var expect = List.of(
                new WisherDto(
                        wisher.getId(), wisher.getInterview().getId(),
                        wisher.getUserId(), wisher.getContactBy(), wisher.isApprove()),
                new WisherDto(
                        wisher1.getId(), wisher1.getInterview().getId(),
                        wisher1.getUserId(), wisher1.getContactBy(), wisher1.isApprove()));
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(expect));
    }

    @Test
    public void whenFindAllWisherDtoThenReturnEmptyList() {
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(Collections.emptyList()));
    }
}