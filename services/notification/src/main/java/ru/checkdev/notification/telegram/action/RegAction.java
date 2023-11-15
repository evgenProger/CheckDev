package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.Profile;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.dto.ProfileTgDTO;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgCall;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 3. Мидл
 * Класс реализует пункт меню регистрации нового пользователя в телеграм бот
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 * Arcady555
 * 06.11.2023
 */
@AllArgsConstructor
@Slf4j
public class RegAction implements Action {
    private static final String ERROR_OBJECT = "error";
    private static final String URL_AUTH_REGISTRATION = "/registration";
    private final TgConfig tgConfig = new TgConfig("tg/", 10);
    private final TgCall tgCall;
    private final UserTelegramService userTelegramService;
    private final InnerMessageService messageService;
    private final String urlSiteAuth;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId();
        var text = "";
        if (userTelegramService.findByChatId(chatId).isPresent()) {
            text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
            return new SendMessage(chatId.toString(), text);
        }
        text = "Введите email для регистрации:";
        return new SendMessage(chatId.toString(), text);
    }

    /**
     * Метод формирует ответ пользователю.
     * Весь метод разбит на 4 этапа проверки.
     * 1. Проверка на соответствие формату Email введенного текста.
     * 2. Проверка, что с данного аккаунта Телеграмм еще не регистрировались на сайте.
     * 3. Отправка данных в сервис Auth и если сервис не доступен сообщаем
     * 4. Если сервис доступен, получаем от него ответ и обрабатываем его.
     * 4.1 ответ при ошибке регистрации
     * 4.2 ответ при успешной регистрации.
     * 5. Сообщение сохраняется в БД
     *
     * @param message Message
     * @return BotApiMethod<Message>
     */
    @Override
    public BotApiMethod<Message> callback(Message message) {
        Object result;
        var text = "";
        var chatId = message.getChatId();
        var email = message.getText();
        var sl = System.lineSeparator();
        if (!tgConfig.isEmail(email)) {
            text = new StringBuilder().append("Email: ").append(email)
                    .append(" не корректный.").append(sl)
                    .append("попробуйте снова.").append(sl)
                    .append("/new").toString();
            return new SendMessage(chatId.toString(), text);
        }
        var username = tgConfig.getNameFromEmail(email);
        var password = tgConfig.getPassword();
        var profile = new Profile(0, username, email,
                password, true, Calendar.getInstance());
        try {
            result = tgCall.doPost(URL_AUTH_REGISTRATION, profile).block();
            var mapObject = tgConfig.getObjectToMap(result);
            if (mapObject.containsKey(ERROR_OBJECT)) {
                text = "Ошибка регистрации: " + mapObject.get(ERROR_OBJECT);
                return new SendMessage(chatId.toString(), text);
            }
            var profileTg = tgConfig.getMapper().convertValue(result, ProfileTgDTO.class);
            profile.setId(profileTg.getId());
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            text = String.format("Сервис не доступен попробуйте позже%s%s", sl, "/start");
            return new SendMessage(chatId.toString(), text);
        }
        text = new StringBuilder().append("Вы зарегистрированы: ").append(sl)
                .append("Имя: ").append(profile.getUsername()).append(sl)
                .append("Email: ").append(profile.getEmail()).append(sl)
                .append("Пароль : ").append(password).append(sl)
                .append(urlSiteAuth).toString();
        userTelegramService.save(new UserTelegram(0, profile.getId(), chatId));
        InnerMessage innerMessage = new InnerMessage();
        innerMessage.setUserId(profile.getId());
        innerMessage.setText(text);
        innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
        messageService.saveMessage(innerMessage);
        return new SendMessage(chatId.toString(), text);
    }
}
