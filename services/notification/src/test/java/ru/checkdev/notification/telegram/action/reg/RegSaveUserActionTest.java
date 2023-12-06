package ru.checkdev.notification.telegram.action.reg;

import org.assertj.core.api.Assertions;
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

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 27.11.2023
 */
class RegSaveUserActionTest {
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
    void whenSaveActionNotEmailThenReturnMessageRepeat() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        RegSaveUserAction regSaveUserAction = new RegSaveUserAction(sessionTg, new FakeTgCallConsole(), userTelegramService, "www");
        BotApiMethod botApiMethod = regSaveUserAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String ls = System.lineSeparator();
        String text = "Пройдите регистрацию заново" + ls + "/new";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenCallBackThenOk() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        String email = "email@email.ru";
        String name = "nameUser";
        sessionTg.put(String.valueOf(chat.getId()), "email", email);
        sessionTg.put(String.valueOf(chat.getId()), "name", name);
        String urlSiteAuth = "www";
        RegSaveUserAction regSaveUserAction = new RegSaveUserAction(sessionTg, new FakeTgCallConsole(), userTelegramService, urlSiteAuth);
        BotApiMethod botApiMethod = regSaveUserAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String passwordInMessage = getPassInMessage(actual, urlSiteAuth);
        String expect = new StringBuilder().append("Вы зарегистрированы: ").append(ls)
                .append("Имя: ").append(name).append(ls)
                .append("Email: ").append(email).append(ls)
                .append("Пароль : ").append(passwordInMessage).append(ls)
                .append(urlSiteAuth).toString();
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenCallBackThenErrorService() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        String email = ERROR_MAIL;
        String name = "nameUser";
        sessionTg.put(String.valueOf(chat.getId()), "email", email);
        sessionTg.put(String.valueOf(chat.getId()), "name", name);
        String urlSiteAuth = "www";
        RegSaveUserAction regSaveUserAction = new RegSaveUserAction(sessionTg, new FakeTgCallConsole(), userTelegramService, urlSiteAuth);
        BotApiMethod botApiMethod = regSaveUserAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = String.format("Сервис не доступен попробуйте позже%s%s", ls, "/start");
        assertThat(actual).isEqualTo(expect);
    }

    private String getPassInMessage(String textMessage, String urlSiteAuth) {
        String startDelimiter = "Пароль : ";
        int startPassIndex = textMessage.indexOf(startDelimiter) + startDelimiter.length();
        int endPassIndex = textMessage.lastIndexOf(System.lineSeparator() + urlSiteAuth);
        return textMessage.substring(startPassIndex, endPassIndex);
    }
}