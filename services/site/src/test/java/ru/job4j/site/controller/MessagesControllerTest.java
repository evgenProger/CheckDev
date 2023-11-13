package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MessagesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenReturnPageMessages() throws Exception {
        this.mockMvc.perform(get("/messages/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("messages/messages"));
    }
}