package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Test;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
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
        MatcherAssert.assertThat(map, is(notify.getKeys()));
    }

    @Test
    void getTemplate() {
        notify.setTemplate("Template");
        String template = "Template";
        MatcherAssert.assertThat(template, is(notify.getTemplate()));
    }

    @Test
    void getEmail() {
        notify.setEmail("arcadypar@mail.ru");
        String email = "arcadypar@mail.ru";
        MatcherAssert.assertThat(email, is(notify.getEmail()));
    }
}