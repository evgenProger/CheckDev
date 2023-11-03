package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import java.sql.Timestamp;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        assertNotNull(botMessage);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        InnerMessage botMessage = new InnerMessage(1, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertNotNull(botMessage);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        InnerMessage botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setId(1);
        assertThat(1, is(botMessage.getId()));
    }
}