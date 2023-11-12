package ru.checkdev.notification.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
public class InnerMessageServiceTest {
    @Autowired
    private InnerMessageService service;

    @Autowired
    private ChatIdService chatIdService;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        List<InnerMessage> list = new ArrayList<>();
        InnerMessage botMessage = this.service.saveMessage(new InnerMessage(1, 10, "text",
                new Timestamp(System.currentTimeMillis()), false));
        List<InnerMessage> result = this.service.findByUserIdAndReadFalse(10);
        assertThat(result).contains(botMessage);
    }
}