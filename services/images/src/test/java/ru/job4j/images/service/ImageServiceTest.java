/**
 * 
 */
package ru.job4j.images.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ru.job4j.images.domain.Image;
import ru.job4j.images.repository.ImageRepository;

/**
 * @author olegbelov
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTest {

	@Autowired
	ImageService imageService;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Test
	public void whenGetByIdShouldReturnCorrectObject() {
		Image image = this.imageRepository.save(new Image("testFileName","testDescription",new byte[] {1,2,3}));
		Image result = this.imageService.getById(image.getId());
		assertThat(result, is(image));
	}

}
