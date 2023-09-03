/**
 *
 */
package ru.job4j.images.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import ru.job4j.images.domain.Image;
import ru.job4j.images.service.ImageService;

/**
 * @author olegbelov
 *
 */

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(method = RequestMethod.POST)
    public Integer upload(HttpServletResponse reponse, HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile multipartFile = multipartRequest.getFile(it.next());
        return imageService.uploadImage(multipartFile);

    }

    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> showImage(@PathVariable Integer imageID) {
        Optional<Image> image = Optional.ofNullable(this.imageService.getById(imageID));
        if (image.isPresent()) {
            String extention = image.get().getFileName().substring(image.get().getFileName().lastIndexOf('.'));
            MediaType responseType = MediaType.IMAGE_JPEG;
            if (("jpg".contains(extention)) || ("jpeg".contains(extention))) {
                responseType = MediaType.IMAGE_JPEG;
            } else if ("png".contains(extention)) {
                responseType = MediaType.IMAGE_PNG;
            } else if ("gif".contains(extention)) {
                responseType = MediaType.IMAGE_GIF;
            }
            return ResponseEntity.ok()
                    .contentLength(image.get().getImageData().length)
                    .contentType(responseType)
                    .body(image.get().getImageData());
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

}
