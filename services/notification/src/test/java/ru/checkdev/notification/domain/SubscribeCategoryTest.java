package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;

public class SubscribeCategoryTest {
    private SubscribeCategory subscribeCategory;

    @BeforeEach
    public void setUp() {
        subscribeCategory = new SubscribeCategory(0, 1, 2);
    }

    @Test
    public void testGetId() {
        assertThat(0).isEqualTo(subscribeCategory.getId());
    }

    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory();
        assertThat(subscribeCategory).isNotNull();
    }

    @Test
    public void testGetUserId() {
        assertThat(1).isEqualTo(subscribeCategory.getUserId());
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertThat(subscribeCategory).isNotNull();
    }

    @Test
    public void testGetCategoryId() {
        assertThat(2).isEqualTo(subscribeCategory.getCategoryId());
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        subscribeCategory.setId(1);
        assertThat(subscribeCategory.getId()).isEqualTo(1);
    }

    @Test
    public void testSetId() {
        subscribeCategory.setId(10);
        assertThat(10).isEqualTo(subscribeCategory.getId());
    }

    @Test
    public void testSetUserId() {
        subscribeCategory.setUserId(11);
        assertThat(11).isEqualTo(subscribeCategory.getUserId());
    }

    @Test
    public void testSetCategoryId() {
        subscribeCategory.setCategoryId(12);
        assertThat(12).isEqualTo(subscribeCategory.getCategoryId());
    }
}