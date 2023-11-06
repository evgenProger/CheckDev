package ru.checkdev.notification.web;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class InnerMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InnerMessageService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    private final InnerMessage botMessage = new InnerMessage(1, 2, "text",
            null, false);

    private final String message = new GsonBuilder().serializeNulls().create().toJson(botMessage);

    @Disabled
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