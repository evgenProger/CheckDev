package ru.checkdev.notification.telegram.action.bind;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import static org.assertj.core.api.Assertions.assertThat;

class BindAccountActionTest {
    /**
     * Поле заведено для отладки тестов
     * При указании данного email пользователя сервис бросает exception
     */
    private static final String ERROR_MAIL = "error@exception.er";

    private final SessionTg sessionTg = new SessionTg();
    private final UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()));

    @Test
    void whenBindThenMessageAccountHasBinded() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        sessionTg.put(String.valueOf(chat.getId()), "email", "email@email.ru");
        sessionTg.put(String.valueOf(chat.getId()), "password", "password");
        BindAccountAction bindAccountAction = new BindAccountAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod botApiMethod = bindAccountAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actualMessage = sendMessage.getText();
        String expectMessage = "Ваш аккаунт CheckDev успешно привязан к данному аккаунту Telegram";
        assertThat(userTelegramService.findByChatId(1L)).isPresent();
        assertThat(actualMessage).isEqualTo(expectMessage);
    }

    @Test
    void whenExceptionAtBindingThenMessageServiceIsUnavailable() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        sessionTg.put(String.valueOf(chat.getId()), "email", ERROR_MAIL);
        BindAccountAction bindAccountAction = new BindAccountAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod botApiMethod = bindAccountAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = String.format("Сервис недоступен, попробуйте позже%s%s", ls, "/start");
        assertThat(actual).isEqualTo(expect);
    }
}