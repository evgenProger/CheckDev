package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.TopicDTO;
import ru.job4j.site.service.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * CheckDev пробное собеседование
 * IndexControllerTest тесты на контроллер IndexController
 *
 * @author Dmitry Stepanov
 * @version 24.09.2023 21:50
 */
@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
class IndexControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoriesService categoriesService;
    @MockBean
    private TopicsService topicsService;
    @MockBean
    private InterviewsService interviewsService;

    @Test
    void injectedNotNull() {
        assertThat(mockMvc).isNotNull();
        assertThat(categoriesService).isNotNull();
        assertThat(topicsService).isNotNull();
        assertThat(interviewsService).isNotNull();
    }

    @Test
    void whenGetIndexPageThenReturnIndex() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void whenGetIndexPageExpectModelAttributeThenOk() throws Exception {
        var token = "1410";
        var topicDTO1 = new TopicDTO();
        topicDTO1.setId(1);
        topicDTO1.setName("topic1");
        var topicDTO2 = new TopicDTO();
        topicDTO2.setId(2);
        topicDTO2.setName("topic2");
        var cat1 = new CategoryDTO(1, "name1");
        var cat2 = new CategoryDTO(2, "name2");
        var listCat = List.of(cat1, cat2);
        var firstInterview = new InterviewDTO(1, 1, 1, 1, 1,
                "interview1", "description1", "contact1",
                "30.02.2024", "09.10.2023", 1, "author1", 1L);
        var secondInterview = new InterviewDTO(2, 1, 1, 2, 1,
                "interview2", "description2", "contact2",
                "30.02.2024", "09.10.2023", 1, "author2", 1L);
        var listInterviews = List.of(firstInterview, secondInterview);
        when(topicsService.getByCategory(cat1.getId())).thenReturn(List.of(topicDTO1));
        when(topicsService.getByCategory(cat2.getId())).thenReturn(List.of(topicDTO2));
        when(topicsService.getAllTopicLiteDTO()).thenReturn(Collections.emptyList());
        when(categoriesService.getMostPopular()).thenReturn(listCat);
        when(interviewsService.getLast()).thenReturn(listInterviews);
        var listBread = List.of(new Breadcrumb("Главная", "/"));
        mockMvc.perform(get("/index/")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(model().attribute("categories", listCat))
                .andExpect(model().attribute("breadcrumbs", listBread))
                .andExpect(model().attribute("topicsLiteMap", Collections.emptyMap()))
                .andExpect(model().attribute("new_interviews", listInterviews))
                .andExpect(view().name("index"));
    }
}