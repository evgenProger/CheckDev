/**
 *
 */
package ru.checkdev.notification.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author olegbelov
 * @since 20.12.2016
 * Arcady555
 * @since 01.11.2023
 */
public class TemplateTest {


    @Test
    public void whenDefaultConstructorNotNull() {
        Template template = new Template();
        assertThat(template).isNotNull();
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        Template template = new Template("TestSubject", "TestBody");
        assertThat(template).isNotNull();
    }

    @Test
    public void whenIDSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setId(1);
        assertThat(template.getId()).isEqualTo(1);
    }


    @Test
    public void whenSubjectTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setSubject("NewSubject");
        assertThat(template.getSubject()).isEqualTo("NewSubject");
    }

    @Test
    public void whenBodyTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setBody("NewBody");
        assertThat("NewBody").isEqualTo(template.getBody());
    }
}