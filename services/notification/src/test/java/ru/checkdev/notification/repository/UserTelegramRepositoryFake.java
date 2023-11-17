package ru.checkdev.notification.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserTelegramRepositoryFake
        extends CrudRepositoryFake<UserTelegram>
        implements UserTelegramRepository {

    @Override
    public Optional<UserTelegram> findByChatId(long chatId) {
        return memory.values().stream()
                .filter(userTelegram -> userTelegram.getChatId() == chatId)
                .findFirst();
    }

    @Override
    public List<Long> findChatIdInUserIds(List<Integer> userIds) {
        return null;
    }

    @Override
    public Optional<UserTelegram> findByUserId(int userId) {
        return Optional.empty();
    }
}
