package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.notification.domain.BotMessage;
import ru.checkdev.notification.service.BotMessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class BotMessageController {

    private final BotMessageService messageService;

    @GetMapping("/{id}")
    public ResponseEntity<List<BotMessage>> findMessage(@PathVariable int id) {
        List<BotMessage> list = messageService.findByUserIdAndReadFalse(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
