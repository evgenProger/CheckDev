package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;
import ru.checkdev.notification.telegram.service.TgCallNoConnect;

import static org.assertj.core.api.Assertions.assertThat;

class UnNotifyActionTest {

    @Test
    void whenUnsubscribe() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        userTelegramService.save(new UserTelegram(1, 1, 1));
        UnNotifyAction unNotifyAction = new UnNotifyAction(new FakeTgCallConsole(), userTelegramService, innerMessageService);
        unNotifyAction.handle(message);
        BotApiMethod<Message> botApiMethod = unNotifyAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        assertThat(sendMessage.getText()).isEqualTo("Вы отписались от уведомлений");
    }

    @Test
    void whenNotConnection() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(0, 10, chat.getId());
        userTelegramService.save(userTelegram);
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        UnNotifyAction unNotifyAction = new UnNotifyAction(new TgCallNoConnect(), userTelegramService, innerMessageService);
        BotApiMethod<Message> botApiMethod = unNotifyAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        assertThat(sendMessage.getText()).isEqualTo("Сервис не доступен попробуйте позже");
    }
}