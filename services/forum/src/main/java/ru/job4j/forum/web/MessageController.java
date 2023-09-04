package ru.checkdev.forum.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.forum.domain.Message;
import ru.checkdev.forum.domain.Subject;
import ru.checkdev.forum.service.MessageService;
import ru.checkdev.forum.service.MessageNotification;
import ru.checkdev.forum.service.SubjectService;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;

/**
 * Controller performing requests related to messages.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RestController
@RequestMapping("/forum/")
public class MessageController extends BaseController {

    /**
     * Service used to perform actions on subjects.
     */
    private final SubjectService subjectService;

    /**
     * Service used to perform actions on messages.
     */
    private final MessageService messageService;

    /**
     * Service used to notify about new message.
     */
    private final MessageNotification messageNotification;

    /**
     * Constructs <code>MessageController</code> object.
     *
     * @param subjectService injected subject service object.
     * @param messageService injected message service object.
     * @param messageNotification injected service object for notification about new message.
     */
    @Autowired
    public MessageController(final SubjectService subjectService, final MessageService messageService,
                             final MessageNotification messageNotification) {
        this.subjectService = subjectService;
        this.messageService = messageService;
        this.messageNotification = messageNotification;
    }

    /**
     * Handler for list all messages request.
     *
     * @param subjectId id of subject that contains found messages. Is is automatically extracted from path variable.
     * @return list of found messages. It is automatically converted to JSON array in response body by Spring MVC.
     */
    @GetMapping("/subject/{subjectId}/message/")
    public List<Message> list(@PathVariable final int subjectId)
            throws Exception {
        this.subjectService.updateCountView(subjectId);
        return this.messageService.getAll(subjectId);
    }

    /**
     * Handler for request used to get one message by provided id.
     *
     * @param id message's id. Is is automatically extracted from path variable.
     * @return found message. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @GetMapping("/message/{id}")
    public Message get(@PathVariable final int id) throws Exception {
        return this.messageService.getById(id);
    }

    /**
     * Handler for add new message request.
     *
     * @param subjectId id of subject that will contain added message. Is is automatically extracted from path variable.
     * @param message new message's data automatically created from JSON object in request body.
     * @param user currently authenticated user data.
     * @return added message. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PostMapping("/subject/{subjectId}/message/")
    @ResponseStatus(HttpStatus.CREATED)
    public Message add(@PathVariable final int subjectId, @RequestBody final Message message,
                       final Principal user)
            throws Exception {
        if (message.getText().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }

        message.setSubject(new Subject(subjectId));
        message.setUserKey(this.getUserKey(user));
        message.setCreateDate(Calendar.getInstance());

        final Message addedMessage = this.messageService.add(message);
        this.messageNotification.sendAsync(addedMessage.getId(), message.getSubject().getId());
        return addedMessage;
    }

    /**
     * Handler for update message request.
     *
     * @param id message's id. It is automatically extracted from path variable.
     * @param messageRequest new message's data automatically created from JSON object in request body.
     * @param user currently authenticated user data.
     * @param servletRequest servlet's request.
     * @return updated message. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PutMapping("/message/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Message update(@PathVariable final int id, @RequestBody final Message messageRequest,
                          final Principal user, final HttpServletRequest servletRequest)
            throws Exception {
        if (messageRequest.getText().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }

        final Message message = this.messageService.getById(id);
        if (!this.checkAccessToResource(servletRequest, user, message.getUserKey())) {
            throw new AccessDeniedException("You don't have access to this resource!");
        }
        message.setText(messageRequest.getText());
        return this.messageService.save(message);
    }

    /**
     * Handler for delete message request.
     *
     * @param id message's id. It is automatically extracted from path variable.
     * @param user currently authenticated user data.
     * @param servletRequest servlet's request.
     */
    @DeleteMapping("/message/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final int id, final Principal user, final HttpServletRequest servletRequest)
            throws Exception {
        final Message message = this.messageService.getById(id);
        if (!this.checkAccessToResource(servletRequest, user, message.getUserKey())) {
            throw new AccessDeniedException("You don't have access to this resource!");
        }

        this.messageService.delete(id);
    }
}