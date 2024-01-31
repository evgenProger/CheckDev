package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setUp() {
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        profile = new Profile(0, "username", "email", "password", true, created);
    }

    @Test
    public void testGetUsername() {
        assertThat("username").isEqualTo(profile.getUsername());
    }

    @Test
    public void testGetEmail() {
        assertThat("email").isEqualTo(profile.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertThat("password").isEqualTo(profile.getPassword());
    }

    @Test
    public void testGetPrivacy() {
        assertThat(true).isEqualTo(profile.isPrivacy());
    }

    @Test
    public void testGetCreated() {
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        assertThat(created).isEqualTo(profile.getCreated());
    }
}