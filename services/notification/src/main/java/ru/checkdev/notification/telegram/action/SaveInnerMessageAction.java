package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.SessionTg;

import java.sql.Timestamp;
import java.util.Optional;

@AllArgsConstructor
public class SaveInnerMessageAction implements Action {
    private final SessionTg sessionTg;
    private final InnerMessageService messageService;
    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var chatId = update.getMessage().getChatId();
        InnerMessage innerMessage = new InnerMessage();
        int userId = Integer.parseInt(sessionTg.get(chatId.toString(), "userId", ""));
        innerMessage.setUserId(userId);
        innerMessage.setText(update.getMessage().getText());
        innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
        messageService.saveMessage(innerMessage);
        return Optional.empty();
    }
}