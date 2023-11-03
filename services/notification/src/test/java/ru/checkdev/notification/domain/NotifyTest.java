package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NotifyTest {
    private Notify notify;

    @BeforeEach
    public void setUp() {
        notify = new Notify();
    }

    @Test
    void getKeys() {
        notify.setKeys(Map.of("qwerty", 12345));
        Map<String, Object> map = new HashMap<>();
        map.put("qwerty", 12345);
        assertThat(notify.getKeys()).isEqualTo(map);
    }

    @Test
    void getTemplate() {
        notify.setTemplate("Template");
        String template = "Template";
        assertThat(template).isEqualTo(notify.getTemplate());
    }

    @Test
    void getEmail() {
        notify.setEmail("arcadypar@mail.ru");
        String email = "arcadypar@mail.ru";
        assertThat(email).isEqualTo(notify.getEmail());
    }
}