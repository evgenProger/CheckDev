package ru.checkdev.notification.telegram.action;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.action.notify.NotifyAction;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import java.util.List;

@Disabled
class SaveInnerMessageActionTest {

    @Test
    void handle() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("test");
        update.setMessage(message);
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        InnerMessageService innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        NotifyAction notifyAction = new NotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        SaveInnerMessageAction saveInnerMessageAction = new SaveInnerMessageAction(
                sessionTg,
                innerMessageService
        );
        BotApiMethod<Message> botApiMethod = saveInnerMessageAction.handle(update).get();
        List rsl = List.of(new InnerMessage(1, 1, "test", null, false));
        Assertions.assertThat(rsl).isEqualTo(innerMessageService.findByUserIdAndReadFalse(1).toArray());
    }
}