package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.InnerMessage;

import java.util.List;

public interface InnerMessageRepository extends CrudRepository<InnerMessage, Integer> {

    List<InnerMessage> findByUserIdAndReadFalse(int id);
}
