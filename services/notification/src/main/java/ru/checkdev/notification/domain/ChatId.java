package ru.checkdev.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cd_chat_id")
public class ChatId {
    @Id
    private int id;
    private String email;
    @OneToMany(mappedBy = "chatId")
    private List<InnerMessage> innerMessages = new ArrayList<>();
}