package ru.checkdev.notification.telegram.action.reg;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 27.11.2023
 */
class RegAskEmailActionTest {
    private UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()));

    @Test
    void whenAskEmailActionChatIdIsPresentThenReturnMessageUserIsPresent() {
        RegAskEmailAction askEmailAction = new RegAskEmailAction(userTelegramService);
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        Update update = new Update();
        update.setMessage(message);
        UserTelegram userTelegram = new UserTelegram(0, 1, chat.getId(), false);
        userTelegramService.save(userTelegram);
        SendMessage sendMessage = (SendMessage) askEmailAction.handle(update).get();
        String expect = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        String actual = sendMessage.getText();
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenAskEmailActionChatIdIsEmptyThenReturnMessageEnterEmail() {
        RegAskEmailAction askEmailAction = new RegAskEmailAction(userTelegramService);
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        Update update = new Update();
        update.setMessage(message);
        SendMessage sendMessage = (SendMessage) askEmailAction.handle(update).get();
        String expect = "Введите email для регистрации:";
        String actual = sendMessage.getText();
        assertThat(actual).isEqualTo(expect);
    }
}