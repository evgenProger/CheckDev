package ru.checkdev.mock.repository;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Feedback;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.enums.StatusInterview;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest()
@RunWith(SpringRunner.class)
class InterviewRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InterviewRepository interviewRepository;

    @BeforeEach
    void clearTables() {
        entityManager.createQuery("delete from cd_feedback").executeUpdate();
        entityManager.createQuery("delete from wisher").executeUpdate();
        entityManager.createQuery("delete from interview").executeUpdate();
        entityManager.clear();
    }

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
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        var interviews = interviewRepository.findByMode(1);
        assertTrue(interviews.size() > 0);
        Assertions.assertEquals(interviews.get(0), interview);
    }

    @Test
    public void whenInterviewNotFoundByType() {
        var interviews = interviewRepository.findByMode(1);
        Assertions.assertEquals(0, interviews.size());
    }

    @Test
    public void whenFindAllInterview() {
        var listInterview = interviewRepository.findAll();
        MatcherAssert.assertThat(listInterview, is(Collections.emptyList()));
    }

    @Test
    public void whenFindAllByUserIdRelatedAndNothingFound() {
        var interview = new Interview();
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        int userId = 2;
        var status = StatusInterview.IS_NEW;
        var page = interviewRepository.findAllByUserIdRelated(userId, status, List.of(1), PageRequest.of(0, 10));
        MatcherAssert.assertThat(page.getTotalElements(), is(0L));
    }

    @Test
    public void whenFindAllByUserIdRelatedAndFound() {
        var interview = new Interview();
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        int userId = 1;
        var status = StatusInterview.IS_NEW;
        var page = interviewRepository.findAllByUserIdRelated(userId, status, List.of(1), PageRequest.of(0, 10));
        MatcherAssert.assertThat(page.getTotalElements(), is(1L));
    }

    @Test
    public void whenInterviewFindByTopicId() {
        var interview = new Interview();
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        var interviews =
                interviewRepository.findByTopicId(1, PageRequest.of(0, 5));
        Assertions.assertTrue(interviews.toList().size() > 0);
        assertTrue(interviews.toList().size() > 0);
        Assertions.assertEquals(interviews.toList().get(0), interview);
    }

    @Test
    public void whenGetInterviewsBy3onPageAndFindByTopicId() {
        var interviewsList = IntStream
                .range(0, 8).mapToObj(i -> {
                    var interview = new Interview();
                    interview.setMode(1);
                    interview.setSubmitterId(1);
                    interview.setTitle(String.format("Interview_%d", i));
                    interview.setAdditional(String.format("Some text_%d", i));
                    interview.setContactBy("Some contact");
                    interview.setApproximateDate("30.02.2024");
                    interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
                    interview.setTopicId(1);
                    interview.setAuthor("author");
                    entityManager.persist(interview);
                    return interview;
                }).toList();
        var firstPage =
                interviewRepository.findByTopicId(1, PageRequest.of(0, 3));
        var secondPage =
                interviewRepository.findByTopicId(1, PageRequest.of(1, 3));
        var thirdPage =
                interviewRepository.findByTopicId(1, PageRequest.of(2, 3));
        var firstPageList = interviewsList.subList(0, 3);
        var secondPageList = interviewsList.subList(3, 6);
        var thirdPageList = interviewsList.subList(6, 8);
        Assertions.assertEquals(firstPage.toList().size(), 3);
        Assertions.assertEquals(firstPage.toList(), firstPageList);
        Assertions.assertEquals(secondPage.toList().size(), 3);
        Assertions.assertEquals(secondPage.toList(), secondPageList);
        Assertions.assertEquals(thirdPage.toList().size(), 2);
        Assertions.assertEquals(thirdPage.toList(), thirdPageList);
    }

    @Test
    public void whenInterviewNotFoundByTopicId() {
        var interviews =
                interviewRepository.findByTopicId(1, PageRequest.of(1, 5));
        Assertions.assertEquals(0, interviews.toList().size());
    }

    @Test
    public void whenUpdateStatusInterviewThenUpdateStatus() {
        var newStatus = StatusInterview.IS_CANCELED;
        var interview = new Interview();
        interview.setMode(1);
        interview.setStatus(StatusInterview.IS_NEW);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        entityManager.clear();
        interviewRepository.updateStatus(interview.getId(), newStatus);
        var interviewInDb = interviewRepository.findById(interview.getId());
        assertThat(interviewInDb.isPresent()).isTrue();
        assertThat(interviewInDb.get().getStatus()).isEqualTo(newStatus);
    }

    @Test
    public void whenUpdateStatusInterviewThenNotUpdateStatus() {
        var newStatus = StatusInterview.IS_CANCELED;
        var interview = new Interview();
        interview.setMode(1);
        interview.setStatus(StatusInterview.IS_NEW);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("contact");
        interview.setApproximateDate("30.02.2070");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        interview.setAuthor("author");
        entityManager.persist(interview);
        entityManager.clear();
        interviewRepository.updateStatus(interview.getId() + 99, newStatus);
        var interviewInDb = interviewRepository.findById(interview.getId());
        assertThat(interviewInDb.isPresent()).isTrue();
        assertThat(interviewInDb.get().getStatus()).isEqualTo(interview.getStatus());
    }

    public void whenInterviewsFoundByTopicIdsList() {
        List<Interview> oddTopicIdsInterviewList = new ArrayList<>();
        List<Interview> evenTopicIdsInterviewList = new ArrayList<>();
        IntStream.range(1, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(i);
            interview.setAuthor("author");
            entityManager.persist(interview);
            if (interview.getTopicId() % 2 == 0) {
                evenTopicIdsInterviewList.add(interview);
            } else {
                oddTopicIdsInterviewList.add(interview);
            }
        });
        var firstOddPage =
                interviewRepository.findByTopicIdIn(List.of(1, 3, 5, 7), PageRequest.of(0, 3));
        var secondEvenPage =
                interviewRepository.findByTopicIdIn(List.of(1, 3, 5, 7), PageRequest.of(1, 3));
        var evenPage =
                interviewRepository.findByTopicIdIn(List.of(2, 4, 6), PageRequest.of(0, 3));
        var firstPageList = oddTopicIdsInterviewList.subList(0, 3);
        var secondPageList = oddTopicIdsInterviewList.subList(3, 4);
        var thirdPageList = evenTopicIdsInterviewList.subList(0, 3);
        Assertions.assertEquals(firstOddPage.toList().size(), 3);
        Assertions.assertEquals(firstOddPage.toList(), firstPageList);
        Assertions.assertEquals(secondEvenPage.toList().size(), 1);
        Assertions.assertEquals(secondEvenPage.toList(), secondPageList);
        Assertions.assertEquals(evenPage.toList().size(), 3);
        Assertions.assertEquals(evenPage.toList(), thirdPageList);
    }

    @Test
    public void whenFindBySubmitterId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd7DifferentTopics();
        var submitter1Interviews =
                interviewRepository.findBySubmitterId(1, PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findBySubmitterId(2, PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findBySubmitterId(1349, PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 0);
    }

    @Test
    public void whenFindByExcludeSubmitterId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd7DifferentTopics();
        var submitter1Interviews =
                interviewRepository.findBySubmitterIdNot(1, PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findBySubmitterIdNot(2, PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findBySubmitterIdNot(1349, PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 7);
    }

    private void inflateInterviewsWith2DifferentSubmittersAnd1Topic() {
        IntStream.range(1, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(i % 2 == 0 ? 1 : 2);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(1);
            interview.setAuthor("Author");
            entityManager.persist(interview);
        });
    }

    private void inflateInterviewsWith2DifferentSubmittersAnd7DifferentTopics() {
        IntStream.range(1, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(i % 2 == 0 ? 1 : 2);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(i);
            interview.setAuthor("Author");
            entityManager.persist(interview);
        });
    }

    @Test
    void whenFindAllNoFeedbackWhenReturnEmptyList() {
        List<Interview> actual = interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(-1);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void whenFindAllByUserIdSubmitterAndUserWisherNoFeedbackWhenReturnListInterview() {
        int submitterId = 1;
        int userWisherId = 2;
        Interview interview = Interview.of()
                .mode(1)
                .submitterId(submitterId)
                .title("title")
                .contactBy("contact")
                .approximateDate("now")
                .createDate(Timestamp.valueOf("2023-11-06 00:00:00"))
                .topicId(1)
                .author("author")
                .build();
        entityManager.persist(interview);
        Wisher wisher = Wisher.of()
                .userId(userWisherId)
                .interview(interview)
                .approve(true)
                .build();
        entityManager.persist(wisher);
        entityManager.clear();
        List<Interview> expected = List.of(interview);
        List<Interview> actualBySubmitter =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(interview.getSubmitterId());
        List<Interview> actualByWisherUser =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(wisher.getUserId());
        assertThat(actualBySubmitter).isEqualTo(expected);
        assertThat(actualByWisherUser).isEqualTo(expected);
    }

    @Test
    void whenFindAllByUserIdSubmitterAndUserWisherNoFeedbackWhenReturnListInterviewSubmitter() {
        int submitterId = 1;
        int userWisherId = 2;
        Interview interview = Interview.of()
                .mode(1)
                .submitterId(submitterId)
                .title("title")
                .contactBy("contact")
                .approximateDate("now")
                .createDate(Timestamp.valueOf("2023-11-06 00:00:00"))
                .topicId(1)
                .author("author")
                .build();
        entityManager.persist(interview);
        Wisher wisher = Wisher.of()
                .userId(userWisherId)
                .interview(interview)
                .approve(true)
                .build();
        entityManager.persist(wisher);
        Feedback feedback = Feedback.of()
                .interview(interview)
                .userId(wisher.getUserId())
                .textFeedback("text")
                .build();
        entityManager.persist(feedback);
        entityManager.clear();
        List<Interview> expected = List.of(interview);
        List<Interview> actualBySubmitter =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(interview.getSubmitterId());
        List<Interview> actualByWisherUser =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(wisher.getUserId());
        assertThat(actualBySubmitter).isEqualTo(expected);
        assertThat(actualByWisherUser.isEmpty()).isTrue();
    }

    @Test
    void whenFindAllByUserIdSubmitterAndUserWisherNoFeedbackWhenReturnListInterviewWisherUser() {
        int submitterId = 1;
        int userWisherId = 2;
        Interview interview = Interview.of()
                .mode(1)
                .submitterId(submitterId)
                .title("title")
                .contactBy("contact")
                .approximateDate("now")
                .createDate(Timestamp.valueOf("2023-11-06 00:00:00"))
                .topicId(1)
                .author("author")
                .build();
        entityManager.persist(interview);
        Wisher wisher = Wisher.of()
                .userId(userWisherId)
                .interview(interview)
                .approve(true)
                .build();
        entityManager.persist(wisher);
        Feedback feedback = Feedback.of()
                .interview(interview)
                .userId(interview.getSubmitterId())
                .textFeedback("text")
                .build();
        entityManager.persist(feedback);
        entityManager.clear();
        List<Interview> expected = List.of(interview);
        List<Interview> actualBySubmitter =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(interview.getSubmitterId());
        List<Interview> actualByWisherUser =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(wisher.getUserId());
        assertThat(actualBySubmitter.isEmpty()).isTrue();
        assertThat(actualByWisherUser).isEqualTo(expected);
    }

    @Test
    void whenFindAllByUserIdSubmitterAndUserWisherApproveFalseNoFeedbackWhenListEmpty() {
        int submitterId = 1;
        int userWisherId = 2;
        Interview interview = Interview.of()
                .mode(1)
                .submitterId(submitterId)
                .title("title")
                .contactBy("contact")
                .approximateDate("now")
                .createDate(Timestamp.valueOf("2023-11-06 00:00:00"))
                .topicId(1)
                .author("author")
                .build();
        entityManager.persist(interview);
        Wisher wisher = Wisher.of()
                .userId(userWisherId)
                .interview(interview)
                .approve(false)
                .build();
        entityManager.persist(wisher);
        entityManager.clear();
        List<Interview> actualBySubmitter =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(interview.getSubmitterId());
        List<Interview> actualByWisherUser =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(wisher.getUserId());
        assertThat(actualBySubmitter.isEmpty()).isTrue();
        assertThat(actualByWisherUser.isEmpty()).isTrue();
    }

    @Test
    void whenFindAllNotFeedbackIsSubmitterFeedbackAndWisherFeedbackThenReturnEmptyList() {
        int submitterId = 1;
        int userWisherId = 2;
        Interview interview = Interview.of()
                .mode(1)
                .submitterId(submitterId)
                .title("title")
                .contactBy("contact")
                .approximateDate("now")
                .createDate(Timestamp.valueOf("2023-11-06 00:00:00"))
                .topicId(1)
                .author("author")
                .build();
        entityManager.persist(interview);
        Wisher wisher = Wisher.of()
                .userId(userWisherId)
                .interview(interview)
                .approve(true)
                .build();
        entityManager.persist(wisher);
        Feedback feedbackSubmitter = Feedback.of()
                .interview(interview)
                .userId(interview.getSubmitterId())
                .textFeedback("textSubmitter")
                .build();
        entityManager.persist(feedbackSubmitter);
        Feedback feedbackWisher = Feedback.of()
                .interview(interview)
                .userId(wisher.getUserId())
                .textFeedback("textUser")
                .build();
        entityManager.persist(feedbackWisher);
        entityManager.clear();
        List<Interview> actualBySubmitter =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(interview.getSubmitterId());
        List<Interview> actualByWisherUser =
                interviewRepository.findAllByUserIdWisherIsApproveAndNoFeedback(wisher.getUserId());
        assertThat(actualBySubmitter.isEmpty()).isTrue();
        assertThat(actualByWisherUser.isEmpty()).isTrue();
    }

    @Test
    public void whenFindBySubmitterIdAndTopicId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd1Topic();
        var submitter1Interviews =
                interviewRepository.findByTopicIdAndSubmitterId(1, 1,
                        PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findByTopicIdAndSubmitterId(1, 2,
                        PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findByTopicIdAndSubmitterId(1, 1349,
                        PageRequest.of(0, 10));
        var submitter4Interviews =
                interviewRepository.findByTopicIdAndSubmitterId(1349, 1,
                        PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 0);
        Assertions.assertEquals(submitter4Interviews.toList().size(), 0);
    }

    @Test
    public void whenFindByTopicIdExcludeSubmitterId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd1Topic();
        var submitter1Interviews =
                interviewRepository.findByTopicIdAndSubmitterIdNot(1, 1,
                        PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findByTopicIdAndSubmitterIdNot(1, 2,
                        PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findByTopicIdAndSubmitterIdNot(1, 1349,
                        PageRequest.of(0, 10));
        var submitter4Interviews =
                interviewRepository.findByTopicIdAndSubmitterIdNot(1349, 1,
                        PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 7);
        Assertions.assertEquals(submitter4Interviews.toList().size(), 0);
    }

    @Test
    public void whenFindBySubmitterIdAndTopicsIdsList() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd7DifferentTopics();
        var submitter1Interviews =
                interviewRepository.findByTopicIdInAndSubmitterId(
                        List.of(1, 2, 3, 4, 5, 6, 7), 1,
                        PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findByTopicIdInAndSubmitterId(
                        List.of(1, 2, 3, 4, 5, 6, 7), 2,
                        PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findByTopicIdInAndSubmitterId(
                        List.of(1, 2, 3, 4, 5, 6, 7), 1349,
                        PageRequest.of(0, 10));
        var submitter4Interviews =
                interviewRepository.findByTopicIdInAndSubmitterId(
                        List.of(11, 22, 33, 44, 55, 66, 77), 1,
                        PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 0);
        Assertions.assertEquals(submitter4Interviews.toList().size(), 0);
    }

    @Test
    public void whenFindByTopicsIdsListExcludeSubmitterId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        inflateInterviewsWith2DifferentSubmittersAnd7DifferentTopics();
        var submitter1Interviews =
                interviewRepository.findByTopicIdInAndSubmitterIdNot(
                        List.of(1, 2, 3, 4, 5, 6, 7), 1,
                        PageRequest.of(0, 10));
        var submitter2Interviews =
                interviewRepository.findByTopicIdInAndSubmitterIdNot(
                        List.of(1, 2, 3, 4, 5, 6, 7), 2,
                        PageRequest.of(0, 10));
        var submitter3Interviews =
                interviewRepository.findByTopicIdInAndSubmitterIdNot(
                        List.of(1, 2, 3, 4, 5, 6, 7), 1349,
                        PageRequest.of(0, 10));
        var submitter4Interviews =
                interviewRepository.findByTopicIdInAndSubmitterIdNot(
                        List.of(11, 22, 33, 44, 55, 66, 77), 1,
                        PageRequest.of(0, 10));
        Assertions.assertEquals(submitter1Interviews.toList().size(), 4);
        Assertions.assertEquals(submitter2Interviews.toList().size(), 3);
        Assertions.assertEquals(submitter3Interviews.toList().size(), 7);
        Assertions.assertEquals(submitter4Interviews.toList().size(), 0);
    }
}