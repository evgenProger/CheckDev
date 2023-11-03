/**
 *
 */
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author olegbelov
 * @since 20.12.2016
 * Arcady555
 * @since 01.11.2023
 */
@SpringBootTest(classes = NtfSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TemplateTest {
    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenDefaultConstructorNotNull() {
        Template template = new Template();
        assertNotNull(template);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        Template template = new Template("TestSubject", "TestBody");
        assertNotNull(template);
    }

    @Test
    public void whenIDSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setId(1);
        assertThat(1, is(template.getId()));
    }


    @Test
    public void whenSubjectTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setSubject("NewSubject");
        assertThat("NewSubject", is(template.getSubject()));
    }

    @Test
    public void whenBodyTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setBody("NewBody");
        assertThat("NewBody", is(template.getBody()));
    }
}