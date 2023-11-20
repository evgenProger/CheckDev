package ru.checkdev.mock.repository;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("mail@mail");
        interview.setApproximateDate("approximate");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
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
                new WisherDto(
                        wisher.getId(), wisher.getInterview().getId(),
                        wisher.getUserId(), wisher.getContactBy(), wisher.isApprove()));
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
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.flush();
        var expect = List.of(
                new WisherDto(
                        wisher.getId(), wisher.getInterview().getId(),
                        wisher.getUserId(), wisher.getContactBy(), wisher.isApprove()),
                new WisherDto(
                        wisher1.getId(), wisher1.getInterview().getId(),
                        wisher1.getUserId(), wisher1.getContactBy(),
                        wisher1.isApprove())
        );
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(expect));
    }

    @Test
    public void whenFindAllWisherDtoThenReturnEmptyList() {
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(Collections.emptyList()));
    }

    @Test
    void whenSetWisherApproveFalseThenReturnWisherNewApprove() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.clear();
        wisherRepository.setWisherApprove(interview.getId(), wisher.getId(), true);
        var wisherInDB = wisherRepository.findById(wisher.getId());
        assertTrue(wisherInDB.isPresent());
        assertTrue(wisherInDB.get().isApprove());
    }

    @Test
    void whenSetWisherApproveThenReturnOldApprove() {
        var userId = 1;
        var wisherId = -1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false);
        entityManager.persist(wisher);
        entityManager.clear();
        wisherRepository.setWisherApprove(interview.getId(), wisherId, true);
        var wisherInDb = wisherRepository.findById(wisher.getId());
        assertTrue(wisherInDb.isPresent());
        assertFalse(wisherInDb.get().isApprove());
    }

    @Test
    void whenFindInterviewsFromWisherByUserId() {
        entityManager.createQuery("delete from wisher").executeUpdate();
        IntStream.range(1, 8).forEach(i -> entityManager.persist(
                new Wisher(0, interview, i % 2 == 0 ? 1 : 2,
                        String.format("user%d@zmail.cd", i), false)));
        entityManager.clear();
        var page1 =
                wisherRepository.findInterviewByUserId(1, PageRequest.of(0, 10));
        var page2 =
                wisherRepository.findInterviewByUserId(2, PageRequest.of(0, 10));
        var page3 =
                wisherRepository.findInterviewByUserId(1349, PageRequest.of(0, 10));
        MatcherAssert.assertThat(page1.toList().size(), is(3));
        MatcherAssert.assertThat(page1.toList().get(0), is(interview));
        MatcherAssert.assertThat(page2.toList().size(), is(4));
        MatcherAssert.assertThat(page3.toList().size(), is(0));
    }

    @Test
    void whenFindInterviewsFromWisherByUserIdApproved() {
        entityManager.createQuery("delete from wisher").executeUpdate();
        IntStream.range(1, 8).forEach(i -> entityManager.persist(
                new Wisher(0, interview, i % 2 == 0 ? 1 : 2,
                        String.format("user%d@zmail.cd", i), true)));
        entityManager.clear();
        var page1 =
                wisherRepository.findInterviewByUserIdApproved(1, PageRequest.of(0, 10));
        var page2 =
                wisherRepository.findInterviewByUserIdApproved(2, PageRequest.of(0, 10));
        var page3 =
                wisherRepository.findInterviewByUserIdApproved(1349, PageRequest.of(0, 10));
        MatcherAssert.assertThat(page1.toList().size(), is(3));
        MatcherAssert.assertThat(page1.toList().get(0), is(interview));
        MatcherAssert.assertThat(page2.toList().size(), is(4));
        MatcherAssert.assertThat(page3.toList().size(), is(0));
    }

    @Test
    void whenFindInterviewsFromWisherByUserIdApprovedAndNothingFound() {
        entityManager.createQuery("delete from wisher").executeUpdate();
        IntStream.range(1, 8).forEach(i -> entityManager.persist(
                new Wisher(0, interview, i % 2 == 0 ? 1 : 2,
                        String.format("user%d@zmail.cd", i), false)));
        entityManager.clear();
        var page1 =
                wisherRepository.findInterviewByUserIdApproved(1, PageRequest.of(0, 10));
        var page2 =
                wisherRepository.findInterviewByUserIdApproved(2, PageRequest.of(0, 10));
        var page3 =
                wisherRepository.findInterviewByUserIdApproved(1349, PageRequest.of(0, 10));
        MatcherAssert.assertThat(page1.toList().size(), is(0));
        MatcherAssert.assertThat(page2.toList().size(), is(0));
        MatcherAssert.assertThat(page3.toList().size(), is(0));
    }

    @Test
    public void whenFindInterviewsFromWisherByUserIdAndByTopicId() {
        entityManager.createQuery("delete from wisher").executeUpdate();
        IntStream.range(1, 8).forEach(i -> entityManager.persist(
                new Wisher(0, interview, i % 2 == 0 ? 1 : 2,
                        String.format("user%d@zmail.cd", i), false)));
        entityManager.clear();
        var page1 =
                wisherRepository.findInterviewByUserIdAndByTopicId(1, 1,
                        PageRequest.of(0, 10));
        var page2 =
                wisherRepository.findInterviewByUserIdAndByTopicId(2, 1,
                        PageRequest.of(0, 10));
        var page3 =
                wisherRepository.findInterviewByUserIdAndByTopicId(1349, 1,
                        PageRequest.of(0, 10));
        var page4 =
                wisherRepository.findInterviewByUserIdAndByTopicId(2, 1349,
                        PageRequest.of(0, 10));
        MatcherAssert.assertThat(page1.toList().size(), is(3));
        MatcherAssert.assertThat(page1.toList().get(0), is(interview));
        MatcherAssert.assertThat(page2.toList().size(), is(4));
        MatcherAssert.assertThat(page3.toList().size(), is(0));
        MatcherAssert.assertThat(page4.toList().size(), is(0));
    }

    @Test
    public void whenFindInterviewsFromWisherByTopicsIdsListAndUserId() {
        entityManager.createQuery("delete from wisher").executeUpdate();
        IntStream.range(1, 8).forEach(i -> {
            var tempInterview = interview;
            tempInterview.setTopicId(i);
            entityManager.persist(
                    new Wisher(0, tempInterview, i % 2 == 0 ? 1 : 2,
                            String.format("user%d@zmail.cd", i), false));
        });
        entityManager.clear();
        var page1 =
                wisherRepository.findInterviewByUserIdAndByTopicIdIn(1,
                        List.of(1, 2, 3, 4, 5, 6, 7), PageRequest.of(0, 10));
        var page2 =
                wisherRepository.findInterviewByUserIdAndByTopicIdIn(2,
                        List.of(1, 2, 3, 4, 5, 6, 7), PageRequest.of(0, 10));
        var page3 =
                wisherRepository.findInterviewByUserIdAndByTopicIdIn(1349,
                        List.of(1, 2, 3, 4, 5, 6, 7), PageRequest.of(0, 10));
        var page4 =
                wisherRepository.findInterviewByUserIdAndByTopicIdIn(1,
                        List.of(17, 28, 39, 50, 61, 72), PageRequest.of(0, 10));
        MatcherAssert.assertThat(page1.toList().size(), is(3));
        MatcherAssert.assertThat(page2.toList().size(), is(4));
        MatcherAssert.assertThat(page3.toList().size(), is(0));
        MatcherAssert.assertThat(page4.toList().size(), is(0));
    }
}