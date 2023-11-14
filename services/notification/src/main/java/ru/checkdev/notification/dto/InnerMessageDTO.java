package ru.checkdev.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class InnerMessageDTO {

    private int id;
    private int userId;
    private String text;
    private Timestamp created;

    public InnerMessageDTO(int id, int userId, String text, Timestamp created) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.created = created;
    }

    public InnerMessageDTO(int id, int userId, String text, Date date) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.created = new Timestamp(date.getTime());
    }
}
