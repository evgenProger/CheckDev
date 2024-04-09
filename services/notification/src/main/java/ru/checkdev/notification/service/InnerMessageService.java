package ru.checkdev.notification.service;

import org.springframework.beans.factory.annotation.Value;
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
public class InnerMessageService {

    private final InnerMessageRepository messageRepository;
    private final UserTelegramService userTelegramService;
    @Value("${service.urlSite}")
    private String urlSite;

    public InnerMessageService(InnerMessageRepository messageRepository, UserTelegramService userTelegramService) {
        this.messageRepository = messageRepository;
        this.userTelegramService = userTelegramService;
    }

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
                saveMessage(new InnerMessage(
                                0,
                                id,
                                "В категории "
                                        + categoryWithTopicDTO.getCategoryName()
                                        + " появилось новое собеседование."
                                        + System.lineSeparator()
                                        + "Ссылка на собеседование: "
                                        + urlSite
                                        + "/interview/"
                                        + categoryWithTopicDTO.getInterviewId(),
                                new Timestamp(System.currentTimeMillis()),
                                false,
                                categoryWithTopicDTO.getInterviewId()
                        )
                )
        );
        topicSubscribersIds.forEach(id ->
                saveMessage(new InnerMessage(
                                0,
                                id,
                                "Появилось новое собеседование по теме "
                                        + categoryWithTopicDTO.getTopicName()
                                        + "."
                                        + System.lineSeparator()
                                        + "Ссылка на собеседование: "
                                        + urlSite
                                        + "/interview/"
                                        + categoryWithTopicDTO.getInterviewId(),
                                new Timestamp(System.currentTimeMillis()),
                                false,
                                categoryWithTopicDTO.getInterviewId()
                        )
                )
        );
    }

    public void send(InnerMessage innerMessage) {
        Optional<UserTelegram> userOptional = userTelegramService.findByUserId(innerMessage.getUserId());
        if (userOptional.isEmpty()) {
            System.out.println("Пользователь не найден!");
        } else {
            UserTelegram user = userOptional.get();
            Long chatId = user.getChatId();
            innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
            innerMessage.setRead(false);
        }
    }

    public void delete(int messageId) {
        messageRepository.deleteById(messageId);
    }

    public void setRead(int messageId) {
        messageRepository.setReadById(messageId);
    }

    public void setReadAll(int userId) {
        messageRepository.setReadAll(userId);
    }
}
