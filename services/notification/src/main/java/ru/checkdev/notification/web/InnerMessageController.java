package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.dto.InnerMessageDTO;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.SubscribeCategoryService;
import ru.checkdev.notification.service.SubscribeTopicService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class InnerMessageController {

    private final InnerMessageService messageService;
    private final SubscribeCategoryService categoryService;
    private final SubscribeTopicService topicService;

    @GetMapping("/{id}")
    public ResponseEntity<List<InnerMessage>> findMessage(@PathVariable int id) {
        List<InnerMessage> list = messageService.findByUserIdAndReadFalse(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/actual/{userId}")
    public ResponseEntity<List<InnerMessageDTO>> findMessageDTO(@PathVariable int userId) {
        List<InnerMessageDTO> result = messageService.findDTOByUserIdAndReadFalse(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/newInterview")
    public ResponseEntity<Void> createMessage(
            @RequestBody CategoryWithTopicDTO categoryWithTopicDTO) {
        List<Integer> categorySubscribersIds =
                categoryService.findUserIdsByCategoryId(categoryWithTopicDTO.getCategoryId());
        List<Integer> topicSubscribersIds =
                topicService.findUserIdsByTopicId(categoryWithTopicDTO.getTopicId());
        messageService.saveMessagesForSubscribers(categoryWithTopicDTO,
                categorySubscribersIds, topicSubscribersIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody InnerMessage innerMessage) {
        messageService.send(innerMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
