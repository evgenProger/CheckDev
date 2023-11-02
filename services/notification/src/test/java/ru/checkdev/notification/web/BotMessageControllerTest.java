package ru.checkdev.notification.web;

import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.BotMessage;
import ru.checkdev.notification.service.BotMessageService;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
public class BotMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BotMessageService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    private final BotMessage botMessage = new BotMessage(1, 2, "text",
            null, false);

    private final String message = new GsonBuilder().serializeNulls().create().toJson(botMessage);

    @Test
    @WithMockUser
    public void whenFindBotMessageByUserId() throws Exception {
        when(service.findByUserIdAndReadFalse(botMessage.getUserId())).thenReturn(List.of(botMessage));
        mockMvc.perform(get("/messages/2"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().string("[" + message + "]"),
                        content().contentType(MediaType.APPLICATION_JSON)
                        );
    }

}