/**
 * 
 */
package ru.job4j.notification.repository;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.notification.domain.Template;

/**
 * @author olegbelov
 * @since 24.12.2016
 */
public interface TemplateRepository extends CrudRepository<Template, Integer> {
    Template findByType(String key);
}
