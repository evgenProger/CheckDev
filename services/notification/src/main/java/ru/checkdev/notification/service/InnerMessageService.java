package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.dto.InnerMessageDTO;
import ru.checkdev.notification.repository.InnerMessageRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InnerMessageService {

    private final InnerMessageRepository messageRepository;
    private final UserTelegramService userTelegramService;

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
                        new Timestamp(System.currentTimeMillis()), false,
                        categoryWithTopicDTO.getInterviewId())));
        topicSubscribersIds.forEach(id ->
                saveMessage(new InnerMessage(0, id,
                        String.format("Появилось новое собеседование по теме %s.",
                                categoryWithTopicDTO.getTopicName()),
                        new Timestamp(System.currentTimeMillis()), false,
                        categoryWithTopicDTO.getInterviewId())));
    }

    public void send(InnerMessage innerMessage) {
        Optional<UserTelegram> userOptional = userTelegramService.findByUserId(innerMessage.getUserId());
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь не найден!!!!!!!!!!!!!!!!!!!!!!!!");
        } else {
            UserTelegram user = userOptional.get();
            Long chatId = user.getChatId();
            innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
            innerMessage.setRead(false);
            saveMessage(innerMessage);
        }
    }
}