package ru.checkdev.notification.web;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.repository.SubscribeCategoryRepositoryFake;
import ru.checkdev.notification.service.SubscribeCategoryService;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeCategoriesControllerFakeTest {

    @Test
    public void whenFindCategoriesByUserId() throws Exception {
        var subscribeCategory = new SubscribeCategory(1, 2, 2);
        var categoryService = new SubscribeCategoryService(new SubscribeCategoryRepositoryFake());
        categoryService.save(subscribeCategory);
        var controller = new SubscribeCategoriesController(categoryService);
        var resp = controller.findCategoriesByUserId(subscribeCategory.getUserId());
        assertThat(resp.getBody()).containsOnly(subscribeCategory.getCategoryId());
    }
}