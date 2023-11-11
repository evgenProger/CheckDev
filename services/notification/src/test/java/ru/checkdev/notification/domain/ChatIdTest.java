package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatIdTest {
    private ChatId chatId;

    @BeforeEach
    public void setUp() {
        List<InnerMessage> innerMessages = new ArrayList<>();
        chatId = new ChatId(1, "email", innerMessages);
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
    void getAndSetInnerMessages() {
        List<InnerMessage> newInnerMessages = new ArrayList<>();
        chatId.setInnerMessages(newInnerMessages);
        Assertions.assertArrayEquals(newInnerMessages.toArray(), chatId.getInnerMessages().toArray());
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