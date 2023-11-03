package ru.checkdev.notification.domain;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.hamcrest.Matchers.is;

class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setUp() {
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        profile = new Profile("username", "email", "password", true, created);
    }

    @Test
    public void testGetUsername() {
        MatcherAssert.assertThat("username", is(profile.getUsername()));
    }

    @Test
    public void testGetEmail() {
        MatcherAssert.assertThat("email", is(profile.getEmail()));
    }

    @Test
    public void testGetPassword() {
        MatcherAssert.assertThat("password", is(profile.getPassword()));
    }

    @Test
    public void testGetPrivacy() {
        MatcherAssert.assertThat(true, is(profile.isPrivacy()));
    }

    @Test
    public void testGetCreated() {
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        MatcherAssert.assertThat(created, is(profile.getCreated()));
    }
}