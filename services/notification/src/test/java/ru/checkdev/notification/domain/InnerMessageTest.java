package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.NtfSrv;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class InnerMessageTest {
    private UserTelegram userTelegram;
    private InnerMessage botMessage;

    @BeforeEach
    public void setUp() {
        botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
    }

    @Test
    public void whenDefaultConstructorNotNull() {
        InnerMessage botMessage1 = new InnerMessage();
        assertThat(botMessage1).isNotNull();
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        assertThat(botMessage).isNotNull();
    }

    @Test
    public void whenIDSetAndGetEquals() {
        botMessage.setId(1);
        assertThat(1).isEqualTo(botMessage.getId());
    }

    @Test
    void getId() {
        Assertions.assertEquals(0, botMessage.getId());
    }

    @Test
    void getText() {
        Assertions.assertEquals("text", botMessage.getText());
    }

    @Test
    void isRead() {
        Assertions.assertFalse(botMessage.isRead());
    }

    @Test
    void setId() {
        botMessage.setId(11);
        Assertions.assertEquals(11, botMessage.getId());
    }

    @Test
    void setText() {
        botMessage.setText("txet");
        Assertions.assertEquals("txet", botMessage.getText());
    }

    @Test
    void setRead() {
        botMessage.setRead(true);
        Assertions.assertTrue(botMessage.isRead());
    }
}