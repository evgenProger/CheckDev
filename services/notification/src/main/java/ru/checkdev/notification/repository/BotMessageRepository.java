package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.BotMessage;

import java.util.List;

public interface BotMessageRepository extends CrudRepository<BotMessage, Integer> {

    List<BotMessage> findByUserIdAndReadFalse(int id);
}
