/**
 *
 */
package ru.checkdev.images.domain;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author olegbelov
 * @since 26.09.2016
 */

@Entity(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String filename;

    private byte[] imagedata;

    /**
     *
     */
    public Image() {
        this.filename = "";
        this.imagedata = null;
    }

    public Image(MultipartFile imageFile) throws IOException {
        this.filename = imageFile.getOriginalFilename();
        this.imagedata = imageFile.getBytes();
    }

    public Image(String fileName, String description, byte[] imageData) {
        this.filename = fileName;
        this.imagedata = imageData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String fileName) {
        this.filename = fileName;
    }

    public byte[] getImageData() {
        return imagedata;
    }

    public void setImageData(byte[] imageData) {
        this.imagedata = imageData;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Image other = (Image) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }


}
