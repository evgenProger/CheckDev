package ru.checkdev.notification.repository;

import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;

import java.util.*;

public class InnerMessageRepositoryFake implements InnerMessageRepository {
    private final Map<Integer, InnerMessage> messages = new HashMap<>();

    @Override
    public List<InnerMessage> findByChatIdAndReadFalse(ChatId chatId) {
        return messages.values().stream()
                .filter(msg -> Objects.equals(msg.getChatId(), chatId))
                .filter(msg -> !msg.isRead())
                .toList();
    }

    @Override
    public <S extends InnerMessage> S save(S entity) {
        messages.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends InnerMessage> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<InnerMessage> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<InnerMessage> findAll() {
        return null;
    }

    @Override
    public Iterable<InnerMessage> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(InnerMessage entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends InnerMessage> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
