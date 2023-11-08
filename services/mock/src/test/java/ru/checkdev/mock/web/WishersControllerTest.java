package ru.checkdev.mock.web;

import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;
import ru.checkdev.mock.service.InterviewService;
import ru.checkdev.mock.service.WisherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
class WishersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WisherService wisherService;

    @MockBean
    private InterviewService interviewService;

    private Interview interview = Interview.of()
            .id(1)
            .mode(2)
            .submitterId(3)
            .title("test_title")
            .additional("test_additional")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(null)
            .build();

    private Wisher wisher = Wisher.of()
            .id(1)
            .interview(interview)
            .userId(1)
            .contactBy("test_contact_by")
            .approve(true)
            .build();

    private String interviewString = new GsonBuilder().serializeNulls().create().toJson(interview);

    private String wisherString = new GsonBuilder().serializeNulls().create().toJson(wisher);

    @Test
    public void whenGetAll() throws Exception {
        when(wisherService.findAll()).thenReturn(List.of(wisher));
        mockMvc.perform(get("/wishers/"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string("[" + wisherString + "]"));
    }

    @Test
    public void whenGetAllThenReturnEmpty() throws Exception {
        when(wisherService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/wishers/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void whenFindByInterviewIdWhenReturnWishers() throws Exception {
        when(interviewService.findById(interview.getId())).thenReturn(Optional.of(interview));
        var wisherList = List.of(wisher);
        when(wisherService.findByInterview(interview)).thenReturn(wisherList);
        mockMvc.perform(get("/wishers/{id}", interview.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(wisherList.size())))
                .andExpect(jsonPath("$[0].id", Matchers.is(wisher.getId())));
    }

    @Test
    public void whenFindByInterviewIdWhenReturnEmpty() throws Exception {
        when(interviewService.findById(interview.getId())).thenReturn(Optional.of(interview));
        when(wisherService.findByInterview(interview)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/wishers/{id}", interview.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void whenFindDtoThenReturnStatusOkAndListWisherDTO() throws Exception {
        var wisherDto1 = new WisherDto(1, 1, 1, "mail@mail", false, 2);
        var wisherDto2 = new WisherDto(2, 2, 2, "mail1@mail", false, 5);
        var expectedList = List.of(wisherDto1, wisherDto2);
        when(wisherService.findAllWisherDto()).thenReturn(expectedList);
        mockMvc.perform(get("/wishers/dto/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(expectedList.size())))
                .andExpect(jsonPath("$[0].id", Matchers.is(wisherDto1.getId())))
                .andExpect(jsonPath("$[1].id", Matchers.is(wisherDto2.getId())));
    }

    @Test
    public void whenFindDtoThenReturnStatusOkAndEmptyList() throws Exception {
        List<WisherDto> expectedList = new ArrayList<>();
        when(wisherService.findAllWisherDto()).thenReturn(expectedList);
        mockMvc.perform(get("/wishers/dto/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(expectedList.size())));
    }

    @Test
    void whenFindDtoByInterviewThenReturnStatusOkAntListWisherDto() throws Exception {
        var wisherDto1 = new WisherDto(1, 1, 1, "mail@mail", false, 2);
        var expectList = List.of(wisherDto1);
        when(wisherService.findWisherDtoByInterviewId(wisherDto1.getInterviewId())).thenReturn(expectList);
        mockMvc.perform(get("/wishers/dto/{id}", wisherDto1.getInterviewId()))
                .andDo(print())
                .andExpect(jsonPath("$.size()", Matchers.is(expectList.size())))
                .andExpect(jsonPath("$[0].id", Matchers.is(wisherDto1.getId())))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void setWisherStatusThenReturnStatusOk() throws Exception {
        var interviewId = 1;
        var wisherId = 2;
        var newStatusId = 3;
        var anyStatusId = 4;
        doNothing().when(wisherService).setWisherStatus(interviewId, wisherId, newStatusId, anyStatusId);
        mockMvc.perform(post("/wishers/status/")
                        .param("interviewId", String.valueOf(interviewId))
                        .param("wisherId", String.valueOf(wisherId))
                        .param("newStatusId", String.valueOf(newStatusId))
                        .param("anyStatusId", String.valueOf(anyStatusId)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void setWisherStatusThenIsUnauthorized() throws Exception {
        var interviewId = 1;
        var wisherId = 2;
        var newStatusId = 3;
        var anyStatusId = 4;
        doNothing().when(wisherService).setWisherStatus(interviewId, wisherId, newStatusId, anyStatusId);
        mockMvc.perform(post("/wishers/status/")
                        .param("interviewId", String.valueOf(interviewId))
                        .param("wisherId", String.valueOf(wisherId))
                        .param("newStatusId", String.valueOf(newStatusId))
                        .param("anyStatusId", String.valueOf(anyStatusId)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}