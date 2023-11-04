package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class InnerMessageService {

    private final InnerMessageRepository messageRepository;

    public List<InnerMessage> findByUserIdAndReadFalse(int id) {
        return messageRepository.findByUserIdAndReadFalse(id);
    }

    public InnerMessage saveMessage(InnerMessage message) {
        return messageRepository.save(message);
    }
}
