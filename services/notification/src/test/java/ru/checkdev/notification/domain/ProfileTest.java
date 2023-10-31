package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setUp() {
        List<RoleDTO> roles = new ArrayList<>();
        roles.add(new RoleDTO(1));
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        profile = new Profile("username", "email", "password", true, created);
    }

    @Test
    public void testGetUsername() {
        assertThat("username", is(profile.getUsername()));
    }

    @Test
    public void testGetEmail() {
        assertThat("email", is(profile.getEmail()));
    }

    @Test
    public void testGetPassword() {
        assertThat("password", is(profile.getPassword()));
    }

    @Test
    public void testGetPrivacy() {
        assertThat(true, is(profile.isPrivacy()));
    }

    @Test
    public void testGetCreated() {
        Calendar created = new Calendar.Builder()
                .setDate(2023, 10, 23)
                .setTimeOfDay(20, 20, 20)
                .build();
        assertThat(created, is(profile.getCreated()));
    }
}