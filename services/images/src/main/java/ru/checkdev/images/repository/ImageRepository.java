/**
 * 
 */
package ru.checkdev.images.repository;

import org.springframework.data.repository.CrudRepository;

import ru.checkdev.images.domain.Image;

/**
 * @author olegbelov
 *
 */
public interface ImageRepository extends CrudRepository<Image, Integer> {

}
