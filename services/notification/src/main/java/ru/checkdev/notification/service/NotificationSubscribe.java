package ru.checkdev.notification.service;


import ru.checkdev.notification.domain.InnerMessage;

import java.util.List;

/**
 * Интерфейс описывает поведение отправки сообщений
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 17.11.2023
 */
public interface NotificationSubscribe<K, V> {
    List<InnerMessage> sendMessage(List<K> targets, V message);
}
