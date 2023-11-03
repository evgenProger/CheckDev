package ru.checkdev.notification.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscribeCategoryTest {
    private SubscribeCategory subscribeCategory;

    @BeforeEach
    public void setUp() {
        subscribeCategory = new SubscribeCategory(0, 1, 2);
    }

    @Test
    public void testGetId() {
        MatcherAssert.assertThat(0, Matchers.is(subscribeCategory.getId()));
    }

    @Test
    public void testGetUserId() {
        MatcherAssert.assertThat(1, Matchers.is(subscribeCategory.getUserId()));
    }

    @Test
    public void testGetCategoryId() {
        MatcherAssert.assertThat(2, Matchers.is(subscribeCategory.getCategoryId()));
    }

    @Test
    public void testSetId() {
        subscribeCategory.setId(10);
        MatcherAssert.assertThat(10, Matchers.is(subscribeCategory.getId()));
    }

    @Test
    public void testSetUserId() {
        subscribeCategory.setUserId(11);
        MatcherAssert.assertThat(11, Matchers.is(subscribeCategory.getUserId()));
    }

    @Test
    public void testSetCategoryId() {
        subscribeCategory.setCategoryId(12);
        MatcherAssert.assertThat(12, Matchers.is(subscribeCategory.getCategoryId()));
    }
}