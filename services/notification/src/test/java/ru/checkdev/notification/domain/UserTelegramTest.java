package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTelegramTest {
    private UserTelegram userTelegram;

    @BeforeEach
    public void setUp() {
        userTelegram = new UserTelegram(1, 10, 555L);
    }

    @Test
    void getId() {
        assertThat(1).isEqualTo(userTelegram.getId());
    }

    @Test
    void getChatId() {
        assertThat(555L).isEqualTo(userTelegram.getChatId());
    }

    @Test
    void setId() {
        userTelegram.setId(2);
        assertThat(2).isEqualTo(userTelegram.getId());
    }

    @Test
    void setChatId() {
        userTelegram.setChatId(333L);
        assertThat(333L).isEqualTo(userTelegram.getChatId());
    }
}