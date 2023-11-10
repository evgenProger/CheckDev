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
public class InnerMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private ChatId chatId;
    private String text;
    private Timestamp created;
    private boolean read;
}
