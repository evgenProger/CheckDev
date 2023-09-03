package ru.job4j.images.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.job4j.images.domain.Image;
import ru.job4j.images.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    /**
     * @param imageRepository
     */
    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image getById(int id) {
        return this.imageRepository.findById(id).get();
    }

    @Override
    public Integer uploadImage(MultipartFile multipartFile) throws IOException {
        Optional<Image> image = Optional.of(new Image(multipartFile));
        if (image.isPresent()) {
            image = Optional.of(this.imageRepository.save(image.get()));
            return (Integer) image.get().getId();
        } else {
            return 0;
        }
    }


}
