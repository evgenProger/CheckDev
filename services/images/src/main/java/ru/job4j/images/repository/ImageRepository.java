/**
 * 
 */
package ru.job4j.images.repository;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.images.domain.Image;

/**
 * @author olegbelov
 *
 */
public interface ImageRepository extends CrudRepository<Image, Integer> {

}
