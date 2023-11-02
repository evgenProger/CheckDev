package ru.checkdev.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.BotMessage;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;
import java.sql.Timestamp;
import java.util.List;
import static org.junit.Assert.*;

@SpringBootTest(classes = NtfSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BotMessageServiceTest {
    @Autowired
    private BotMessageService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        BotMessage botMessage = this.service.saveMessage(new BotMessage(1, 2, "text",
                new Timestamp(System.currentTimeMillis()), false));
        List<BotMessage> result = this.service.findByUserIdAndReadFalse(2);
        assertTrue(result.contains(botMessage));
    }
}