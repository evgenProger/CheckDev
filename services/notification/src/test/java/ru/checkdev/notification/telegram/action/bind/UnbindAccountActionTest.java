package ru.checkdev.notification.telegram.action.bind;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;

import static org.assertj.core.api.Assertions.assertThat;

class UnbindAccountActionTest {

    private final UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()));

    @Test
    void whenUnbindWithUserTelegramThenOk() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        UserTelegram userTelegram = new UserTelegram(0, 0, 1L);
        userTelegramService.save(userTelegram);
        UnbindAccountAction unbindAccountAction = new UnbindAccountAction(userTelegramService);
        BotApiMethod botApiMethod = unbindAccountAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actualMessage = sendMessage.getText();
        String expectMessage = "Ваш аккаунт CheckDev отвязан от текущего аккаунта Telegram";
        assertThat(userTelegramService.findByChatId(1L)).isEmpty();
        assertThat(actualMessage).isEqualTo(expectMessage);
    }

    @Test
    void whenUnbindWithoutUserTelegramThenMessageAccountIsNotBind() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        update.setMessage(message);
        UnbindAccountAction unbindAccountAction = new UnbindAccountAction(userTelegramService);
        BotApiMethod botApiMethod = unbindAccountAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actualMessage = sendMessage.getText();
        String expectMessage = "К данному аккаунту телеграм не привязан аккаунт CheckDev";
        assertThat(actualMessage).isEqualTo(expectMessage);
    }

}