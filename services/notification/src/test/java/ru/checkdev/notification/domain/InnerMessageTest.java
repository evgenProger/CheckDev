package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerMessageTest {

    @Test
    public void whenDefaultConstructorNotNull() {
        InnerMessage botMessage1 = new InnerMessage();
        assertThat(botMessage1).isNotNull();
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertThat(botMessage).isNotNull();
    }

    @Test
    public void whenIDSetAndGetEquals() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setId(1);
        assertThat(1).isEqualTo(botMessage.getId());
    }

    @Test
    void getId() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertThat(0).isEqualTo(botMessage.getId());
    }

    @Test
    void getText() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertThat("text").isEqualTo(botMessage.getText());
    }

    @Test
    void isRead() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        assertThat(botMessage.isRead()).isEqualTo(false);
    }

    @Test
    void setId() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setId(11);
        assertThat(11).isEqualTo(botMessage.getId());
    }

    @Test
    void setText() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setText("txet");
        assertThat("txet").isEqualTo(botMessage.getText());
    }

    @Test
    void setRead() {
        var botMessage = new InnerMessage(0, 1, "text",
                new Timestamp(System.currentTimeMillis()), false);
        botMessage.setRead(true);
        assertThat(botMessage.isRead()).isEqualTo(true);
    }
}