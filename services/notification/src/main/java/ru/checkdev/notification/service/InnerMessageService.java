package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.dto.InnerMessageDTO;
import ru.checkdev.notification.repository.InnerMessageRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class InnerMessageService {

    private final InnerMessageRepository messageRepository;

    public List<InnerMessage> findByUserIdAndReadFalse(int id) {
        return messageRepository.findByUserIdAndReadFalse(id);
    }

    public List<InnerMessageDTO> findDTOByUserIdAndReadFalse(int userId) {
        return messageRepository.findMessageDTOByUserIdAndReadFalse(userId);
    }

    public InnerMessage saveMessage(InnerMessage message) {
        return messageRepository.save(message);
    }

    public void saveMessagesForSubscribers(CategoryWithTopicDTO categoryWithTopicDTO,
                             List<Integer> categorySubscribersIds,
                             List<Integer> topicSubscribersIds) {
        categorySubscribersIds.forEach(id ->
                saveMessage(new InnerMessage(0, id,
                        String.format("В категории \"%s\" появилось новое собеседование.",
                                categoryWithTopicDTO.getCategoryName()),
                        new Timestamp(System.currentTimeMillis()), false)));
        topicSubscribersIds.forEach(id ->
                saveMessage(new InnerMessage(0, id,
                        String.format("Появилось новое собеседование по теме %s.",
                                categoryWithTopicDTO.getTopicName()),
                        new Timestamp(System.currentTimeMillis()), false)));
    }
}
