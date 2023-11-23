package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.config.TgConfig;
import java.util.Optional;

@AllArgsConstructor
public class RegAction20 implements Action {
    private final SessionTg sessionTg;
    private final TgConfig tgConfig = new TgConfig("tg/", 10);

    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var text = "";
        var chatId = update.getMessage().getChatId();
        var email = sessionTg.get(chatId.toString(), "email", "");
        var sl = System.lineSeparator();
        if (!tgConfig.isEmail(email)) {
            text = new StringBuilder().append("Email: ").append(email)
                    .append(" не корректный.").append(sl)
                    .append("попробуйте снова.").append(sl)
                    .append("/new").toString();
            return Optional.of(new SendMessage(chatId.toString(), text));
        } else {
            return Optional.empty();
        }
    }
}