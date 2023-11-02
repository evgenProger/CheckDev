package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.BotMessage;
import ru.checkdev.notification.repository.BotMessageRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class BotMessageService {

    private final BotMessageRepository messageRepository;

    public List<BotMessage> findByUserIdAndReadFalse(int id) {
        return messageRepository.findByUserIdAndReadFalse(id);
    }

    public BotMessage saveMessage(BotMessage message) {
        return messageRepository.save(message);
    }
}
