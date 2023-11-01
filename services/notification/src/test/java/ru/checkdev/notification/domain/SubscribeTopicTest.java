package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = NtfSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SubscribeTopicTest {
    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic();
        assertNotNull(subscribeTopic);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        assertNotNull(subscribeTopic);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        subscribeTopic.setId(1);
        assertThat(1, is(subscribeTopic.getId()));
    }
}