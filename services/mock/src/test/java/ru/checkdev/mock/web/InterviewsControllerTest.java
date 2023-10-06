package ru.checkdev.mock.web;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;

import java.sql.Timestamp;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
class InterviewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewService service;

    private Interview interview = Interview.of()
            .id(1)
            .typeInterview(2)
            .submitterId(3)
            .title("test_title")
            .description("test_description")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(null)
            .build();

    private String string = new GsonBuilder().serializeNulls().create().toJson(interview);

    @Test
    @WithMockUser
    public void whenGetAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(interview));
        mockMvc.perform(get("/interviews/"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string("[" + string + "]"));
    }

}