package ru.job4j.site.controller.interview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.*;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.InterviewsService;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
public class InterviewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InterviewsService interviewsService;
    @MockBean
    private AuthService authService;

    @Test
    public void whenShowAllInterviews() throws Exception {
        var token = "1410";
        var userInfo = new UserInfoDTO();
        var role = new Role();
        role .setId(1);
        role.setValue("ROLE_USER");
        List<InterviewDTO> interviews = IntStream.range(0, 3).mapToObj(i -> {
            var interview = new InterviewDTO();
            interview.setId(i);
            interview.setTypeInterview(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setDescription("Some text");
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate("06.10.2023");
            return interview;
        }).toList();
        userInfo.setRoles(List.of(role));
        when(interviewsService.getAll(token)).thenReturn(interviews);
        when(authService.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(get("/interviews/").sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("interviews"))
                .andExpect(model().attribute("interviews", interviews))
                .andExpect(model().attribute("current_page", "interviews"))
                .andExpect(model().attribute("breadcrumbs", List.of(
                        new Breadcrumb("Главная", "/index"),
                        new Breadcrumb("Собеседования", "/interviews/"))));
    }
}