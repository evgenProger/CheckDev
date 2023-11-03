/**
 *
 */
package ru.checkdev.notification.service;

import ru.checkdev.notification.domain.Notify;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class NotificationServiceTest {

    @Test
    public void whenReadQeueu() {
        TemplateService templates = new TemplateService(null, null) {
            @Override
            public Notify send(Notify user) {
                System.out.println(user.getEmail());
                System.out.println(user.getTemplate());
                return user;
            }
        };
    }


}
