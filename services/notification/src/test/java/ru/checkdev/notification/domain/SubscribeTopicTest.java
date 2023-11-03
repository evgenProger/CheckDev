package ru.checkdev.notification.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscribeTopicTest {
    private SubscribeTopic subscribeTopic;
    @BeforeEach
    public void setUp() {
        subscribeTopic = new SubscribeTopic(0, 1, 2);
    }

    @Test
    public void testGetId() {
        MatcherAssert.assertThat(0, Matchers.is(subscribeTopic.getId()));
    }

    @Test
    public void testGetUserId() {
        MatcherAssert.assertThat(1, Matchers.is(subscribeTopic.getUserId()));
    }

    @Test
    public void testGetCategoryId() {
        MatcherAssert.assertThat(2, Matchers.is(subscribeTopic.getTopicId()));
    }

    @Test
    public void testSetId() {
        subscribeTopic.setId(10);
        MatcherAssert.assertThat(10, Matchers.is(subscribeTopic.getId()));
    }

    @Test
    public void testSetUserId() {
        subscribeTopic.setUserId(11);
        MatcherAssert.assertThat(11, Matchers.is(subscribeTopic.getUserId()));
    }

    @Test
    public void testSetCategoryId() {
        subscribeTopic.setTopicId(12);
        MatcherAssert.assertThat(12, Matchers.is(subscribeTopic.getTopicId()));
    }
}