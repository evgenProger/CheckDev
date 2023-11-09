package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.ChatId;

import java.util.Optional;

public interface ChatIdRepository extends CrudRepository<ChatId, Integer> {
}
