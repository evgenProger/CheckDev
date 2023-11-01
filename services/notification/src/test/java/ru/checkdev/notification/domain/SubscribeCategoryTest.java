package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@SpringBootTest(classes = NtfSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SubscribeCategoryTest {
    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory();
        assertNotNull(subscribeCategory);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertNotNull(subscribeCategory);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        subscribeCategory.setId(1);
        assertThat(1, is(subscribeCategory.getId()));
    }

    @Test
    public void testGetId() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertThat(0, is(subscribeCategory.getId()));
    }

    @Test
    public void testGetUserId() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertThat(1, is(subscribeCategory.getUserId()));
    }

    @Test
    public void testGetCategoryId() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 2);
        assertThat(2, is(subscribeCategory.getCategoryId()));
    }
}