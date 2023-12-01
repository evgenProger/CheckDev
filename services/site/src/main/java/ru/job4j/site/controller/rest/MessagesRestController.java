package ru.job4j.site.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.service.MessageService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@RestController
@AllArgsConstructor
@RequestMapping("/messages_rest")
public class MessagesRestController {

    private final MessageService messageService;

    @DeleteMapping("/delete/{messageId}")
    public void delete(@PathVariable int messageId, HttpServletRequest request)
            throws JsonProcessingException {
        messageService.delete(getToken(request), messageId);
    }
}
