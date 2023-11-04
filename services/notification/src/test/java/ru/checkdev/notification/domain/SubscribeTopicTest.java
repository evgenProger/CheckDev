package ru.checkdev.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeTopicTest {
    private SubscribeTopic subscribeTopic;

    @BeforeEach
    public void setUp() {
        subscribeTopic = new SubscribeTopic(0, 1, 2);
    }

    @Test
    public void testGetId() {
        assertThat(0).isEqualTo(subscribeTopic.getId());
    }

    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic();
        assertThat(subscribeTopic).isNotNull();
    }

    @Test
    public void testGetUserId() {
        assertThat(subscribeTopic.getUserId()).isEqualTo(1);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        assertThat(subscribeTopic).isNotNull();
    }

    @Test
    public void testGetCategoryId() {
        assertThat(subscribeTopic.getTopicId()).isEqualTo(2);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        subscribeTopic.setId(1);
        assertThat(subscribeTopic.getId()).isEqualTo(1);
    }

    @Test
    public void testSetId() {
        subscribeTopic.setId(10);
        assertThat(subscribeTopic.getId()).isEqualTo(10);
    }

    @Test
    public void testSetUserId() {
        subscribeTopic.setUserId(11);
        assertThat(subscribeTopic.getUserId()).isEqualTo(11);
    }

    @Test
    public void testSetCategoryId() {
        subscribeTopic.setTopicId(12);
        assertThat(subscribeTopic.getTopicId()).isEqualTo(12);
    }
}