package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscribeCategoryServiceTest {
    @Autowired
    private SubscribeCategoryService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenGetAllSubCatReturnContainsValue() {
        SubscribeCategory subscribeCategory1 = this.service.save(new SubscribeCategory(2, 1, 1));
        SubscribeCategory subscribeCategory2 = this.service.save(new SubscribeCategory(1, 2, 2));
        List<SubscribeCategory> result = this.service.findAll();
        assertArrayEquals(result.toArray(), List.of(subscribeCategory1, subscribeCategory2).toArray());
    }

    @Test
    public void whenDeleteSubCatItIsNotExist() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(2, 3, 3));
        subscribeCategory = this.service.delete(subscribeCategory);
        List<SubscribeCategory> result = this.service.findAll();
        assertFalse(result.contains(subscribeCategory));
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(1, 2, 2));
        List<Integer> result = this.service.findCategoriesByUserId(subscribeCategory.getUserId());
        assertEquals(result, List.of(2));
        this.service.delete(subscribeCategory);
    }
}