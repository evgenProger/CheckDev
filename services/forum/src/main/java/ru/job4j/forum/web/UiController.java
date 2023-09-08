package ru.checkdev.forum.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.checkdev.forum.domain.Category;
import ru.checkdev.forum.domain.Message;
import ru.checkdev.forum.domain.Subject;
import ru.checkdev.forum.service.CategoryService;
import ru.checkdev.forum.service.MessageService;
import ru.checkdev.forum.service.SubjectService;

import java.util.List;

/**
 * Controller performing GET requests and providing UI using thymeleaf.
 *
 * @author Lightstar
 * @since 06.06.2017
 */
@Controller
@RequestMapping("/forum/")
public class UiController extends BaseController {

    /**
     * Service used to perform actions on categories.
     */
    private final CategoryService categoryService;

    /**
     * Service used to perform actions on subjects.
     */
    private final SubjectService subjectService;

    /**
     * Service used to perform actions on messages.
     */
    private final MessageService messageService;

    /**
     * Constructs <code>UiController</code> object.
     *
     * @param categoryService injected category service object.
     * @param subjectService injected subject service object.
     * @param messageService injected message service object.
     */
    @Autowired
    public UiController(final CategoryService categoryService, final SubjectService subjectService,
                        final MessageService messageService) {
        this.categoryService = categoryService;
        this.subjectService = subjectService;
        this.messageService = messageService;
    }

    /**
     * Handler for ping request used to check live status of this microservice.
     *
     * @return response body.
     */
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "{}";
    }

    /**
     * Handler for forum main page showing list of all categories.
     *
     * @param modelAndView object containing model attributes and view name.
     * @return filled <code>modelAndView</code> object.
     */
    @GetMapping({"/", "/index.html"})
    public ModelAndView categories(final ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService.getAll());
        modelAndView.setViewName("forum/index");

        return modelAndView;
    }

    /**
     * Handler for subjects page showing list of all subjects for a given category.
     *
     * @param categoryId category id whose subjects are shown.
     * @param modelAndView object containing model attributes and view name.
     * @return filled <code>modelAndView</code> object.
     * @throws Exception thrown when data can't be retrieved.
     */
    @GetMapping("/subjects.html")
    public ModelAndView subjects(@RequestParam final int categoryId,
                                 @RequestParam(required = false, defaultValue = "0") final int page,
                                 final ModelAndView modelAndView) throws Exception {
        final Category category = this.categoryService.getById(categoryId);
        final Page<Subject> subjects = this.subjectService.getAll(categoryId, page);

        modelAndView.addObject("category", category);
        modelAndView.addObject("subjects", subjects);
        modelAndView.setViewName("forum/subjects");

        return modelAndView;
    }

    /**
     * Handler for messages page showing list of all messages for a given subject.
     *
     * @param subjectId subject id whose messages are shown.
     * @param modelAndView object containing model attributes and view name.
     * @return filled <code>modelAndView</code> object.
     * @throws Exception thrown when data can't be retrieved.
     */
    @GetMapping("/messages.html")
    public ModelAndView messages(@RequestParam final int subjectId, final ModelAndView modelAndView) throws Exception {
        this.subjectService.updateCountView(subjectId);

        final Subject subject = this.subjectService.getById(subjectId);
        final List<Message> messages = this.messageService.getAll(subjectId);

        modelAndView.addObject("category", subject.getCategory());
        modelAndView.addObject("subject", subject);
        modelAndView.addObject("messages", messages);
        modelAndView.setViewName("forum/messages");

        return modelAndView;
    }

    /**
     * Handler for result page showing list of all new message.
     *
     * @param modelAndView object containing model attributes and view name.
     * @return filled <code>modelAndView</code> object.
     */
    @GetMapping({"/result.html"})
    public ModelAndView subjectNewMessage(final ModelAndView modelAndView,
                                    @RequestParam(required = false, defaultValue = "0") final int page) throws Exception {
        modelAndView.addObject("subjects", this.subjectService.getAllSubject(page));
        modelAndView.setViewName("forum/result");

        return modelAndView;
    }
}