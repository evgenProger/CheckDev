package ru.checkdev.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cd_message")
public class InnerMessage implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private String text;
    private Timestamp created;
    private boolean read;
    private int interviewId;

    public InnerMessage(int id, int userId, String text, Timestamp created, boolean read) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.created = created;
        this.read = read;
    }
}
