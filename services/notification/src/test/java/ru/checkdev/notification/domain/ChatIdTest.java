package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatIdTest {
    private ChatId chatId;

    @BeforeEach
    public void setUp() {
        chatId = new ChatId(1, 10, "email");
    }

    @Test
    void getId() {
        assertThat(1).isEqualTo(chatId.getId());
    }

    @Test
    void getEmail() {
        assertThat("email").isEqualTo(chatId.getEmail());
    }

    @Test
    void setId() {
        chatId.setId(2);
        assertThat(2).isEqualTo(chatId.getId());
    }

    @Test
    void setEmail() {
        chatId.setEmail("noEmail");
        assertThat("noEmail").isEqualTo(chatId.getEmail());
    }
}