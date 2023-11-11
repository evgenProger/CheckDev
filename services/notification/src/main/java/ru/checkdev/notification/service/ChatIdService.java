package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.repository.ChatIdRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatIdService {
    private final ChatIdRepository repository;

    public boolean save(ChatId chatId) {
        try {
            repository.save(chatId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void delete(int id) {
        Optional<ChatId> chatIdOptional = repository.findById(id);
        if (chatIdOptional.isPresent()) {
            ChatId chatId = chatIdOptional.get();
            repository.delete(chatId);
        }
    }

    public Optional<ChatId> findById(int id) {
        return repository.findById(id);
    }
}