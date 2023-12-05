package ru.checkdev.notification.telegram.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
public interface Action {
    Optional<BotApiMethod> handle(Update update);

    default Iterator<? extends Action> bindingActions() {
        return new ArrayList<Action>().iterator();
    }
}
