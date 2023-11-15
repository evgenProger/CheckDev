package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.repository.SubscribeCategoryRepositoryFake;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeCategoryServiceFakeTest {

    @Test
    public void whenGetAllSubCatReturnContainsValue() {
        var service = new SubscribeCategoryService(new SubscribeCategoryRepositoryFake());
        var subscribeCategory1 = service.save(new SubscribeCategory(2, 1, 1));
        var subscribeCategory2 = service.save(new SubscribeCategory(1, 2, 2));
        assertThat(service.findAll())
                .contains(subscribeCategory1, subscribeCategory2);
    }

    @Test
    public void whenDeleteSubCatItIsNotExist() {
        var service = new SubscribeCategoryService(new SubscribeCategoryRepositoryFake());
        var subscribeCategory = service.save(new SubscribeCategory(2, 3, 3));
        service.delete(subscribeCategory);
        assertThat(service.findAll())
                .doesNotContain(subscribeCategory);
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        var service = new SubscribeCategoryService(new SubscribeCategoryRepositoryFake());
        var subscribeCategory = service.save(new SubscribeCategory(1, 2, 2));
        assertThat(service.findCategoriesByUserId(subscribeCategory.getUserId()))
                .isEqualTo(List.of(2));
        service.delete(subscribeCategory);
    }

    @Test
    public void whenFindUserIdsByCategoryId() {
        var service = new SubscribeCategoryService(new SubscribeCategoryRepositoryFake());
        var subscribeCategory1 = service
                .save(new SubscribeCategory(1, 1, 4));
        var subscribeCategory2 = service
                .save(new SubscribeCategory(2, 2, 4));
        assertThat(service.findUserIdsByCategoryId(4).size())
                .isEqualTo(2);
        service.delete(subscribeCategory1);
        service.delete(subscribeCategory2);
    }
}