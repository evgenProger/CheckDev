package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.UserTelegramRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTelegramService {
    private final UserTelegramRepository repository;

    public boolean save(UserTelegram userTelegram) {
        try {
            repository.save(userTelegram);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void delete(int id) {
        Optional<UserTelegram> chatIdOptional = repository.findById(id);
        if (chatIdOptional.isPresent()) {
            UserTelegram userTelegram = chatIdOptional.get();
            repository.delete(userTelegram);
        }
    }

    /**
     * Метод возвращает Optional<UserTelegram> по chatId
     *
     * @param chatId UserTelegram chatId long
     * @return Optional<UserTelegram>
     */
    public Optional<UserTelegram> findByChatId(long chatId) {
        return repository.findByChatId(chatId);
    }
}