package ru.job4j.site.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.dto.FilterDTO;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.service.InterviewsService;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
public class InterviewsRequestManagerTest {

    private final InterviewsService interviewsService = mock(InterviewsService.class);
    private final InterviewsRequestManager interviewsRequestManager =
            new InterviewsRequestManager(interviewsService);

    private final List<InterviewDTO> allInterviews = IntStream.range(1, 4).mapToObj(i ->
            new InterviewDTO(0, 1, 1, i,
            "Title", "Text", "contact",
            "30.02.2024", "03.11.2023",
            i, "Author")).toList();

    private final List<InterviewDTO> singleTopicInterviews = IntStream.range(1, 4).mapToObj(i ->
            new InterviewDTO(0, 1, 1, i,
                    "Title", "Text", "contact",
                    "30.02.2024", "03.11.2023",
                    2, "Author")).toList();

    private final List<InterviewDTO> singleSubmitterInterviews = IntStream.range(1, 4).mapToObj(i ->
            new InterviewDTO(0, 1, 1, 1,
                    "Title", "Text", "contact",
                    "30.02.2024", "03.11.2023",
                    i, "Author")).toList();

    private final List<InterviewDTO> singleSubmitterAndTopicInterviews = IntStream.range(1, 4).mapToObj(i ->
            new InterviewDTO(0, 1, 1, 1,
                    "Title", "Text", "contact",
                    "30.02.2024", "03.11.2023",
                    2, "Author")).toList();

    private final String token = "1234567";

    @Test
    void checkThatStuffNotNull() {
        assertNotNull(interviewsService);
        assertNotNull(interviewsRequestManager);
    }

    @Test
    void whenRequestedInterviewsWithEmptyFilter() throws JsonProcessingException {
        var filter = new FilterDTO(0, 0, 0, 0);
        var page = new PageImpl<>(allInterviews);
        when(interviewsService.getAll(token, 0, 20)).thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByTopicId() throws JsonProcessingException {
        var filter = new FilterDTO(1, 0, 1, 0);
        var page = new PageImpl<>(singleTopicInterviews);
        when(interviewsService.getByTopicId(1, 0, 20)).thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByTopicsIds() throws JsonProcessingException {
        var filter = new FilterDTO(1, 1, 0, 0);
        var page = new PageImpl<>(allInterviews);
        var topicsList = List.of(1, 2, 3);
        when(interviewsService.getByTopicsIds(topicsList, 0, 20)).thenReturn(page);
        assertEquals(interviewsRequestManager
                .findByTopicsList(topicsList, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsWithSingleSubmitter() throws JsonProcessingException {
        var filter = new FilterDTO(1, 0, 0, 1);
        var page = new PageImpl<>(singleSubmitterInterviews);
        when(interviewsService.getBySubmitterId(1, false, 0, 20)).thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsWithSingleTopicAndSubmitter() throws JsonProcessingException {
        var filter = new FilterDTO(1, 0, 1, 1);
        var page = new PageImpl<>(singleSubmitterAndTopicInterviews);
        when(interviewsService
                .getByTopicIdAndSubmitterId(1, 1, false, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsExcludedSubmitter() throws JsonProcessingException {
        var filter = new FilterDTO(6, 0, 0, 3);
        var page = new PageImpl<>(allInterviews);
        when(interviewsService
                .getBySubmitterId(6, true, 0, 20)).thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByTopicIdExcludedSubmitter() throws JsonProcessingException {
        var filter = new FilterDTO(6, 0, 1, 3);
        var page = new PageImpl<>(singleTopicInterviews);
        when(interviewsService
                .getByTopicIdAndSubmitterId(1, 6, true, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenInterviewsRequestedByTopicsListExcludedSubmitter() throws JsonProcessingException {
        var filter = new FilterDTO(6, 1, 0, 3);
        var page = new PageImpl<>(allInterviews);
        var topicsList = List.of(1, 2, 3);
        when(interviewsService
                .getByTopicsIdsAndSubmitterId(topicsList, 6, true, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.findByTopicsList(topicsList, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByUserIdAsWisher() throws JsonProcessingException {
        var filter = new FilterDTO(1, 0, 0, 2);
        var page = new PageImpl<>(allInterviews);
        when(interviewsService.getByUserIdAsWisher(1, false, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByTopicIdAndUserAsWisher() throws JsonProcessingException {
        var filter = new FilterDTO(1, 0, 1, 2);
        var page = new PageImpl<>(singleTopicInterviews);
        when(interviewsService.getByTopicIdAndUserIdAsWisher(1, 1, false, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenRequestedInterviewsByTopicIdExcludeUserAsWisher() throws JsonProcessingException {
        var filter = new FilterDTO(6, 0, 1, 4);
        var page = new PageImpl<>(singleTopicInterviews);
        when(interviewsService.getByTopicIdAndUserIdAsWisher(1, 6, true, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.find(token, filter, 0, 20), page);
    }

    @Test
    void whenInterviewsRequestedByTopicsListAndUserAsWisher() throws JsonProcessingException {
        var filter = new FilterDTO(1, 1, 0, 2);
        var page = new PageImpl<>(allInterviews);
        var topicsList = List.of(1, 2, 3);
        when(interviewsService
                .getByTopicsIdsAndUserIdAsWisher(topicsList, 1, false, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.findByTopicsList(topicsList, filter, 0, 20), page);
    }

    @Test
    void whenInterviewsRequestedByTopicsListExcludedUserAsWisher() throws JsonProcessingException {
        var filter = new FilterDTO(6, 1, 0, 4);
        var page = new PageImpl<>(allInterviews);
        var topicsList = List.of(1, 2, 3);
        when(interviewsService
                .getByTopicsIdsAndUserIdAsWisher(topicsList, 6, true, 0, 20))
                .thenReturn(page);
        assertEquals(interviewsRequestManager.findByTopicsList(topicsList, filter, 0, 20), page);
    }
}
