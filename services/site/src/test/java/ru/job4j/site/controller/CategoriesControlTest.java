package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;

import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
class CategoriesControlTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenShowCategoriesWithoutUserInfo() throws Exception {
        var token = "1410";
        mockMvc.perform(get("/categories/").sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("categories/categories"))
                .andExpect(model().attribute("breadcrumbs", List.of(
                        new Breadcrumb("Главная", "/index")
                )));
    }
}