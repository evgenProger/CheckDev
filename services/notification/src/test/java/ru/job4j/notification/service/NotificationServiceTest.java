/**
 * 
 */
package ru.job4j.notification.service;

import static org.hamcrest.core.Is.is;

import org.junit.Ignore;
import org.junit.Test;

import ru.job4j.notification.domain.Notify;


/**
 * @author olegbelov
 *
 */
 @Ignore
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
