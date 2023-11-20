package ru.checkdev.mock.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.enums.StatusInterview;
import ru.checkdev.mock.repository.InterviewRepository;
import ru.checkdev.mock.repository.WisherRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = InterviewService.class)
@RunWith(SpringRunner.class)
class InterviewServiceTest {

    @MockBean
    private InterviewRepository interviewRepository;

    @MockBean
    private WisherRepository wisherRepository;

    @Autowired
    private InterviewService interviewService;

    private Interview interview = Interview.of()
            .id(1)
            .mode(2)
            .status(StatusInterview.IS_NEW)
            .submitterId(3)
            .title("test_title")
            .additional("test_additional")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(new Timestamp(System.currentTimeMillis()))
            .build();

    @Test
    public void whenSaveAndGetTheSame() {
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenSaveAndGetEmpty() {
        when(interviewRepository.save(any(Interview.class))).thenThrow(new DataIntegrityViolationException(""));
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenGetAll() {
        List<Interview> interviews = IntStream.range(0, 5).mapToObj(i -> {
            var interview = new Interview();
            interview.setId(i);
            interview.setMode(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional("Some text");
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            return interview;
        }).toList();
        var page = new PageImpl<>(interviews);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        when(interviewRepository.findAll(pageable)).thenReturn(page);
        var actual = interviewService.findPaging(0, 5);
        assertThat(actual, is(page));
    }

    @Test
    public void whenGetAllByUserIdRelated() {
        int userId = 1;
        var status = StatusInterview.IS_NEW;
        List<Interview> interviews = IntStream.range(0, 5).mapToObj(i -> {
            var interview = new Interview();
            interview.setId(i);
            interview.setMode(1);
            interview.setSubmitterId(userId);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional("Some text");
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            return interview;
        }).toList();
        var page = new PageImpl<>(interviews);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        when(wisherRepository.findInterviewByUserIdApproved(userId, Pageable.unpaged())).thenReturn(Page.empty());
        when(interviewRepository.findAllByUserIdRelated(userId, status, List.of(), pageable)).thenReturn(page);
        var actual = interviewService.findPagingByUserIdRelated(0, 5, userId);
        assertThat(actual, is(page));
        assertThat(actual.getTotalElements(), is(5L));
    }

    @Test
    public void whenGetAllByUserIdRelatedAndNothingFound() {
        int userId = 1;
        var status = StatusInterview.IS_NEW;
        Page<Interview> page = Page.empty();
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        when(wisherRepository.findInterviewByUserIdApproved(userId, Pageable.unpaged())).thenReturn(Page.empty());
        when(interviewRepository.findAllByUserIdRelated(userId, status, List.of(), pageable)).thenReturn(page);
        var actual = interviewService.findPagingByUserIdRelated(0, 5, userId);
        assertThat(actual, is(page));
        assertThat(actual.getTotalElements(), is(0L));
    }

    @Test
    public void whenFindByIdIsCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        var actual = interviewService.findById(1);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenFindByIdIsNotCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = interviewService.findById(1);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenUpdateIsCorrect() {
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenDeleteIsCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        var actual = interviewService.delete(interview);
        assertThat(actual, is(true));
    }

    @Test
    public void whenDeleteIsNotCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = interviewService.delete(interview);
        assertThat(actual, is(false));
    }

    @Test
    public void whenGetAllWithTopicIdIsNull() {
        Interview interviewWithTopicId = interview;
        interviewWithTopicId.setTopicId(1);
        when(interviewRepository.findAll()).thenReturn(List.of(interviewWithTopicId));
        var actual = interviewService.findAll();
        assertThat(actual, is(List.of(interviewWithTopicId)));
    }

    @Test
    public void whenFindByTypeAllWithTopicIdIsNull() {
        Interview interviewWithTopicId = interview;
        interviewWithTopicId.setTopicId(1);
        interviewWithTopicId.setMode(1);
        when(interviewRepository.findByMode(1)).thenReturn(List.of(interviewWithTopicId));
        var actual = interviewService.findByMode(1);
        assertThat(actual, is(List.of(interviewWithTopicId)));
    }

    @Test
    public void whenUpdateStatusThenTrue() {
        doNothing().when(interviewRepository).updateStatus(1, StatusInterview.IN_PROGRESS);
        var actual = interviewService.updateStatus(1, StatusInterview.IN_PROGRESS);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenGetByTopicsIds() {
        List<Interview> oddTopicIdsInterviewList = new ArrayList<>();
        List<Interview> evenTopicIdsInterviewList = new ArrayList<>();
        IntStream.range(0, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(i + 1);
            if (interview.getTopicId() % 2 == 0) {
                evenTopicIdsInterviewList.add(interview);
            } else {
                oddTopicIdsInterviewList.add(interview);
            }
        });
        var evenPage = new PageImpl<>(evenTopicIdsInterviewList);
        var oddPage = new PageImpl<>(oddTopicIdsInterviewList);
        when(interviewRepository.findByTopicIdIn(List.of(2, 4, 6), PageRequest.of(0, 5)))
                .thenReturn(evenPage);
        when(interviewRepository.findByTopicIdIn(List.of(1, 3, 5, 7), PageRequest.of(0, 5)))
                .thenReturn(oddPage);
        assertThat(interviewService.findByTopicsIds(List.of(2, 4, 6), 0, 5), is(evenPage));
        assertThat(interviewService.findByTopicsIds(List.of(1, 3, 5, 7), 0, 5), is(oddPage));
    }

    @Test
    public void whenFindBySubmitterId() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateListsByInterviewsWith7topicsAnd2Submitters(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findBySubmitterId(1, PageRequest.of(0, 5)))
                .thenReturn(firstSubmitterPage);
        when(interviewRepository.findBySubmitterId(2, PageRequest.of(0, 5)))
                .thenReturn(secondSubmitterPage);
        assertThat(interviewService.findBySubmitterId(1, 0, 5), is(firstSubmitterPage));
        assertThat(interviewService.findBySubmitterId(2, 0, 5), is(secondSubmitterPage));
    }

    @Test
    public void whenFindByExcludeSubmitterId() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateListsByInterviewsWith7topicsAnd2Submitters(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findBySubmitterIdNot(1, PageRequest.of(0, 5)))
                .thenReturn(secondSubmitterPage);
        when(interviewRepository.findBySubmitterIdNot(2, PageRequest.of(0, 5)))
                .thenReturn(firstSubmitterPage);
        assertThat(interviewService.findBySubmitterIdNot(1, 0, 5), is(secondSubmitterPage));
        assertThat(interviewService.findBySubmitterIdNot(2, 0, 5), is(firstSubmitterPage));
    }

    @Test
    public void whenFindBySubmitterIdAndTopicId() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateInterviewListsWith2SubmittersAnd2Topics(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findByTopicIdAndSubmitterId(2, 2,
                PageRequest.of(0, 5))).thenReturn(firstSubmitterPage);
        when(interviewRepository.findByTopicIdAndSubmitterId(1, 1,
                PageRequest.of(0, 5))).thenReturn(secondSubmitterPage);
        assertThat(interviewService
                .findByTopicIdAndSubmitterId(2, 2, 0, 5),
                is(firstSubmitterPage));
        assertThat(interviewService
                .findByTopicIdAndSubmitterId(1, 1, 0, 5),
                is(secondSubmitterPage));
    }

    @Test
    public void whenFindByTopicIdExcludeSubmitterId() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateInterviewListsWith2SubmittersAnd2Topics(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findByTopicIdAndSubmitterIdNot(2, 2,
                PageRequest.of(0, 5))).thenReturn(secondSubmitterPage);
        when(interviewRepository.findByTopicIdAndSubmitterIdNot(1, 1,
                PageRequest.of(0, 5))).thenReturn(firstSubmitterPage);
        assertThat(interviewService
                        .findByTopicIdAndSubmitterIdNot(2, 2, 0, 5),
                is(secondSubmitterPage));
        assertThat(interviewService
                        .findByTopicIdAndSubmitterIdNot(1, 1, 0, 5),
                is(firstSubmitterPage));
    }

    private void inflateInterviewListsWith2SubmittersAnd2Topics(
            List<Interview> firstSubmitterInterviewList,
            List<Interview> secondSubmitterInterviewList) {
        IntStream.range(1, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(i % 2 == 0 ? 1 : 2);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(i % 2 == 0 ? 1 : 2);
            if (interview.getSubmitterId() == 1) {
                firstSubmitterInterviewList.add(interview);
            } else {
                secondSubmitterInterviewList.add(interview);
            }
        });
    }

    @Test
    public void whenFindBySubmitterIdAndTopicsIdsList() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateListsByInterviewsWith7topicsAnd2Submitters(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findByTopicIdInAndSubmitterId(
                List.of(1, 2, 3, 4, 5, 6, 7), 2,
                PageRequest.of(0, 5))).thenReturn(firstSubmitterPage);
        when(interviewRepository.findByTopicIdInAndSubmitterId(
                List.of(1, 2, 3, 4, 5, 6, 7), 1,
                PageRequest.of(0, 5))).thenReturn(secondSubmitterPage);
        assertThat(interviewService.findByTopicsListIdAndSubmitterId(
                List.of(1, 2, 3, 4, 5, 6, 7), 2, 0, 5),
                is(firstSubmitterPage));
        assertThat(interviewService.findByTopicsListIdAndSubmitterId(
                List.of(1, 2, 3, 4, 5, 6, 7), 1, 0, 5),
                is(secondSubmitterPage));
    }

    @Test
    public void whenFindByTopicsIdsListAndExcludeSubmitterId() {
        List<Interview> firstSubmitterInterviewList = new ArrayList<>();
        List<Interview> secondSubmitterInterviewList = new ArrayList<>();
        inflateListsByInterviewsWith7topicsAnd2Submitters(
                firstSubmitterInterviewList, secondSubmitterInterviewList);
        var firstSubmitterPage = new PageImpl<>(secondSubmitterInterviewList);
        var secondSubmitterPage = new PageImpl<>(firstSubmitterInterviewList);
        when(interviewRepository.findByTopicIdInAndSubmitterIdNot(
                List.of(1, 2, 3, 4, 5, 6, 7), 2,
                PageRequest.of(0, 5))).thenReturn(secondSubmitterPage);
        when(interviewRepository.findByTopicIdInAndSubmitterIdNot(
                List.of(1, 2, 3, 4, 5, 6, 7), 1,
                PageRequest.of(0, 5))).thenReturn(firstSubmitterPage);
        assertThat(interviewService.findByTopicsListIdAndSubmitterIdNot(
                        List.of(1, 2, 3, 4, 5, 6, 7), 2, 0, 5),
                is(secondSubmitterPage));
        assertThat(interviewService.findByTopicsListIdAndSubmitterIdNot(
                        List.of(1, 2, 3, 4, 5, 6, 7), 1, 0, 5),
                is(firstSubmitterPage));
    }

    private void inflateListsByInterviewsWith7topicsAnd2Submitters(
            List<Interview> firstSubmitterInterviewList,
            List<Interview> secondSubmitterInterviewList
    ) {
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
            if (interview.getSubmitterId() == 1) {
                firstSubmitterInterviewList.add(interview);
            } else {
                secondSubmitterInterviewList.add(interview);
            }
        });
    }

    @Test
    public void whenFindByUserIdAsWisher() {
        var page = new PageImpl<>(List.of(interview));
        when(wisherRepository
                .findInterviewByUserId(1, PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService.findByUserIdAsWisher(1, 0, 10), is(page));
    }

    @Test
    public void whenFindByUserIdAsNotWisher() {
        var page = new PageImpl<>(List.of(interview));
        when(interviewRepository
                .findInterviewByUserIdNot(1, PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService.findByUserIdAsNotWisher(1, 0, 10), is(page));
    }

    @Test
    public void whenFindByUserIdAsWisherAndTopicId() {
        var page = new PageImpl<>(List.of(interview));
        when(wisherRepository
                .findInterviewByUserIdAndByTopicId(1, 1, PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService
                .findByUserIdAsWisherByTopic(1, 1, 0, 10), is(page));
    }

    @Test
    public void whenFindByUserIdAsNotWisherAndTopicId() {
        var page = new PageImpl<>(List.of(interview));
        when(interviewRepository
                .findInterviewByUserIdNotAndByTopicId(1, 1, PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService
                .findByUserIdAsNotWisherByTopic(1, 1, 0, 10), is(page));
    }

    @Test
    public void whenFindByUserIdAsWisherAndTopicsIdsList() {
        var page = new PageImpl<>(List.of(interview));
        when(wisherRepository
                .findInterviewByUserIdAndByTopicIdIn(1, List.of(1, 2), PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService
                .findByUserIdAsWisherByTopicList(1, List.of(1, 2), 0, 10), is(page));
    }

    @Test
    public void whenFindByUserIdAsNotWisherAndTopicsIdsList() {
        var page = new PageImpl<>(List.of(interview));
        when(interviewRepository
                .findInterviewByUserIdNotAndByTopicIdIn(1, List.of(1, 2), PageRequest.of(0, 10)))
                .thenReturn(page);
        assertThat(interviewService
                .findByUserIdAsNotWisherByTopicList(1, List.of(1, 2), 0, 10), is(page));
    }

    @Test
    void whenFindAllIdByNoFeedbackThenReturnListInterview() {
        int submitterId = 1;
        int wisherUser = 2;
        Interview interview = Interview.of()
                .id(1)
                .submitterId(submitterId)
                .build();
        List<Interview> expected = List.of(interview);
        doReturn(expected).when(interviewRepository).findAllByUserIdWisherIsApproveAndNoFeedback(wisherUser);
        List<Interview> actual = interviewService.findAllIdByNoFeedback(wisherUser);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenFindAllIdByNoFeedbackThenReturnEmptyList() {
        int wisherUser = 2;
        doReturn(Collections.emptyList()).when(interviewRepository).findAllByUserIdWisherIsApproveAndNoFeedback(wisherUser);
        List<Interview> actual = interviewService.findAllIdByNoFeedback(wisherUser);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void whenGetAllWithStatusNew() {
        Interview interviewNewStatus = interview;
        var status = StatusInterview.IS_NEW;
        when(interviewRepository.findNewInterviews(status)).thenReturn(List.of(interviewNewStatus));
    }

    @Test
    public void whenPutNotNewStatusGetEmptyList() {
        var status = StatusInterview.IS_NEW;
        when(interviewRepository.findNewInterviews(status)).thenReturn(List.of());
        List<Interview> actual = interviewService.findNewInterview();
        assertThat(actual.isEmpty()).isTrue();

    }
}