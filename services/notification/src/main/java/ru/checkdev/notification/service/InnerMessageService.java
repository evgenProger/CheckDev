package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class InnerMessageService {

    private final InnerMessageRepository messageRepository;

    public List<InnerMessage> findByChatIdAndReadFalse(ChatId chatId) {
        return messageRepository.findByChatIdAndReadFalse(chatId);
    }

    public InnerMessage saveMessage(InnerMessage message) {
        return messageRepository.save(message);
    }
}
