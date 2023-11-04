/**
 *
 */
package ru.checkdev.notification.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.domain.Template;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TemplateServiceTest {

    @Autowired
    TemplateService templateService;

    @Disabled
    @Test
    public void whenGetAllTemplatesReturnContainsValue() {
        Template template = this.templateService.save(new Template("TestSubject", "TestBody"));
        List<Template> result = this.templateService.findAll();
        assertThat(result).contains(template);
    }

    @Disabled
    @Test
    public void requestByIDReturnCorrectValue() {
        Template template = this.templateService.save(new Template("TestSubjectByID", "TestBodyByID"));
        Template result = this.templateService.getById(template.getId());
        assertThat(result).isEqualTo(template);
    }

    @Disabled
    @Test
    public void whenDeleteTemplateItIsNotExist() {
        Template template = this.templateService.save(new Template("TestSubjectForDelete", "TestBodyForDelete"));
        this.templateService.delete(template.getId());
        List<Template> result = this.templateService.findAll();
        assertThat(result).contains(template);
    }

}
