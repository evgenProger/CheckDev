package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class InnerMessageTest {

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenDefaultConstructorNotNull() {
        InnerMessage botMessage = new InnerMessage();
        assertThat(botMessage).isNotNull();
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        ChatId chatId = new ChatId(1, null, null);
        InnerMessage botMessage = new InnerMessage(1, chatId, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertThat(botMessage).isNotNull();
    }

    @Test
    public void whenIDSetAndGetEquals() {
        ChatId chatId = new ChatId(1, null, null);
        InnerMessage botMessage = new InnerMessage(0, chatId, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setId(1);
        assertThat(1).isEqualTo(botMessage.getId());
    }
}