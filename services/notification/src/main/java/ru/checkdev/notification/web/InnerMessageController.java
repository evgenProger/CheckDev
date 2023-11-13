package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.service.ChatIdService;
import ru.checkdev.notification.service.InnerMessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class InnerMessageController {

    private final InnerMessageService messageService;
    private final ChatIdService chatIdService;

    @GetMapping("/{id}")
    public ResponseEntity<List<InnerMessage>> findMessage(@PathVariable int id) {
        List<InnerMessage> list = messageService.findByUserIdAndReadFalse(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
