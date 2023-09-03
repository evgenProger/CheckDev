package ru.job4j.images.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ru.job4j.images.domain.Image;

public interface ImageService {

	Image getById(int id);

	Integer uploadImage(MultipartFile multipartFile) throws IOException;

}
