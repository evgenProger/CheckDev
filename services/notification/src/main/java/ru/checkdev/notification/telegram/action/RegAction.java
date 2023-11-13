package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.service.ChatIdService;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.domain.Profile;
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
    private static final String URL_AUTH_CURRENT = "/person/currentForTg/";
    private final TgConfig tgConfig = new TgConfig("tg/", 8);
    private final TgCall tgCall;;
    private final ChatIdService chatIdService;
    private final InnerMessageService messageService;
    private final String urlSiteAuth;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatIdString = message.getChatId().toString();
        var text ="";
        if (chatIdService.findById(Integer.parseInt(chatIdString)).isPresent()) {
            text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
            return new SendMessage(chatIdString, text);
        }
        text = "Введите email для регистрации:";
        return new SendMessage(chatIdString, text);
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
        var chatIdString = message.getChatId().toString();
        var email = message.getText();
        var sl = System.lineSeparator();
        var username = getNameFromEmail(email);
        var password = tgConfig.getPassword();
        var profile = new Profile(0, username, email, password, true, Calendar.getInstance());

        if (!tgConfig.isEmail(email)) {
            text = "Email: " + email + " не корректный." + sl
                    + "попробуйте снова." + sl
                    + "/new";
            return new SendMessage(chatIdString, text);
        }
        ChatId chatId = new ChatId();
        chatId.setId(Integer.parseInt(chatIdString));
        chatId.setEmail(email);

        if (!chatIdService.save(chatId)) {
            text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
            return new SendMessage(chatIdString, text);
        }
        try {
            result = tgCall.doPost(URL_AUTH_REGISTRATION, profile).block();
            profile = tgCall
                    .doGet(URL_AUTH_CURRENT + chatId.getEmail()).block();
            chatId.setUserId(profile.getId());
            chatIdService.save(chatId);
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже" + sl
                    + "/start";
            return new SendMessage(chatIdString, text);
        }

        var mapObject = tgConfig.getObjectToMap(result);

        if (mapObject.containsKey(ERROR_OBJECT)) {
            text = "Ошибка регистрации: " + mapObject.get(ERROR_OBJECT);
            return new SendMessage(chatIdString, text);
        }

        text = "Вы зарегистрированы: " + sl
                + "Имя: " + username + sl
                + "Email: " + email + sl
                + "Пароль : " + password + sl
                + urlSiteAuth;
        InnerMessage innerMessage = new InnerMessage();
        innerMessage.setUserId(profile.getId());
        innerMessage.setText(text);
        innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
        messageService.saveMessage(innerMessage);
        return new SendMessage(chatIdString, text);
    }

    private String getNameFromEmail(String email) {
        String[] array = email.split("@");
        return array[0];
    }
}
