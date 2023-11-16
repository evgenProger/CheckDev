package ru.checkdev.notification.telegram.service;

public interface Notificator {

    String sendMessage(long chatId, String message);
}
