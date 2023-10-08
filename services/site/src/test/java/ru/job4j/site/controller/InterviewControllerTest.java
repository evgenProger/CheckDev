package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.*;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.TopicsService;

import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
public class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InterviewService interviewService;
    @MockBean
    private TopicsService topicsService;
    @MockBean
    private AuthService authService;

    @Test
    public void whenShowDetails() throws Exception {
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Собеседования", "/interviews/"),
                new Breadcrumb("Some interview", "/interview/1"));
        var token = "1410";
        var userInfo = new UserInfoDTO();
        InterviewDTO interview = new InterviewDTO();
        interview.setId(1);
        interview.setTitle("Some interview");
        interview.setDescription("Some description");
        interview.setTypeInterview(4);
        var status = StatusInterview.values()[interview.getTypeInterview()].getInfo();
        when(authService.userInfo(token)).thenReturn(userInfo);
        when(interviewService.getById(token, 1)).thenReturn(interview);
        mockMvc.perform(get("/interview/1").sessionAttr("token", token))
                .andDo(print())
                .andExpect(model().attribute("interview", interview))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(model().attribute("userInfo", userInfo))
                .andExpect(model().attribute("status", status))
                .andExpect(status().isOk())
                .andExpect(view().name("/interview/details"));
    }

    @Test
    public void whenOpenCreateForm() throws Exception {
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Категории", "/categories/"),
                new Breadcrumb("Some category", "/topics/Some category/1"),
                new Breadcrumb("Some topic", "/topic/Some category/1/1"));
        var token = "1410";
        var userInfo = new UserInfoDTO();
        var topic = new TopicDTO();
        topic.setId(1);
        topic.setName("Some topic");
        topic.setText("Some text");
        topic.setCreated(Calendar.getInstance());
        topic.setUpdated(Calendar.getInstance());
        CategoryDTO category = new CategoryDTO();
        category.setId(1);
        category.setName("Some category");
        topic.setCategory(category);
        when(authService.userInfo(token)).thenReturn(userInfo);
        when(topicsService.getById(1)).thenReturn(topic);
        mockMvc.perform(get("/interview/createForm")
                        .sessionAttr("token", token)
                        .param("topicId", "1"))
                .andDo(print())
                .andExpect(model().attribute("category", topic.getCategory()))
                .andExpect(model().attribute("topic", topic))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(status().isOk())
                .andExpect(view().name("interview/createForm"));
    }

    @Test
    public void whenInterviewCreated() throws Exception {
        var token = "1410";
        var userInfo = new UserInfoDTO();
        var role = new Role();
        role.setId(1);
        role.setValue("ROLE_USER");
        userInfo.setRoles(List.of(role));
        InterviewDTO interview = new InterviewDTO();
        interview.setId(1);
        interview.setTitle("Some interview");
        interview.setDescription("Some description");
        interview.setContactBy("Some contact");
        interview.setApproximateDate("30.02.2024");
        interview.setCreateDate("06.10.2023");
        when(authService.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(post("/interview/create")
                        .flashAttr("interviewDTO", interview)
                        .param("topicId", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/interviews/"));
    }

}