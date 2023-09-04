package ru.checkdev.forum.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.forum.domain.Category;
import ru.checkdev.forum.domain.Subject;
import ru.checkdev.forum.service.CategoryService;
import ru.checkdev.forum.service.SubjectService;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Calendar;

/**
 * Controller performing requests related to subjects.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RestController
@RequestMapping("/forum/")
public class SubjectController extends BaseController {

    /**
     * Service used to perform actions on categories.
     */
    private final CategoryService categoryService;

    /**
     * Service used to perform actions on subjects.
     */
    private final SubjectService subjectService;

    /**
     * Constructs <code>SubjectController</code> object.
     *
     * @param categoryService injected category service object.
     * @param subjectService injected subject service object.
     */
    @Autowired
    public SubjectController(final CategoryService categoryService, final SubjectService subjectService) {
        this.categoryService = categoryService;
        this.subjectService = subjectService;
    }

    /**
     * Handler for list all subjects request.
     *
     * @param categoryId id of category that contains found subjects. Is is automatically extracted from path variable.
     * @return list of found subjects. It is automatically converted to JSON array in response body by Spring MVC.
     */
    @GetMapping("/category/{categoryId}/subject/")
    public Page<Subject> list(@PathVariable final int categoryId,
                              @SortDefault(value = "createDate")
                              @RequestParam(required = false, defaultValue = "0") final int page)
            throws Exception {
        return this.subjectService.getAll(categoryId, page);
    }


    /**
     * Handler for request used to get one subject by provided id.
     *
     * @param id subject's id. Is is automatically extracted from path variable.
     * @return found subject. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @GetMapping("/subject/{id}")
    public Subject get(@PathVariable final int id) throws Exception {
        return this.subjectService.getById(id);
    }

    /**
     * Handler for add new subject request.
     *
     * @param categoryId id of category that will contain added subject. Is is automatically extracted from path
     *                   variable.
     * @param subject new subject's data automatically created from JSON object in request body.
     * @param user currently authenticated user data.
     * @return added subject. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PostMapping("/category/{categoryId}/subject/")
    @ResponseStatus(HttpStatus.CREATED)
    public Subject add(@PathVariable final int categoryId, @RequestBody final Subject subject,
                       final Principal user) {
        if (subject.getName().isEmpty() || subject.getBrief().isEmpty() || subject.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }
        subject.setCategory(new Category(categoryId));
        subject.setUserKey(this.getUserKey(user));
        subject.setLastUserKey(subject.getUserKey());
        subject.setCreateDate(Calendar.getInstance());
        subject.setLastDate(subject.getCreateDate());
        subject.setCountMessage(0);
        subject.setCountView(0);
        return this.subjectService.save(subject);
    }

    /**
     * Handler for update subject request.
     *
     * @param id subject's id. It is automatically extracted from path variable.
     * @param subjectRequest new subject's data automatically created from JSON object in request body.
     * @param user currently authenticated user data.
     * @param servletRequest servlet's request.
     * @return updated subject. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PutMapping("/subject/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Subject update(@PathVariable final int id, @RequestBody final Subject subjectRequest,
                          final Principal user, final HttpServletRequest servletRequest)
            throws Exception {
        if (subjectRequest.getName().isEmpty() || subjectRequest.getBrief().isEmpty() ||
                subjectRequest.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }

        final Subject subject = this.subjectService.getById(id);
        if (!this.checkAccessToResource(servletRequest, user, subject.getUserKey())) {
            throw new AccessDeniedException("You don't have access to this resource!");
        }
        subject.setName(subjectRequest.getName());
        subject.setBrief(subjectRequest.getBrief());
        subject.setDescription(subjectRequest.getDescription());
        return this.subjectService.save(subject);
    }

    /**
     * Handler for delete subject request.
     *
     * @param id subject's id. It is automatically extracted from path variable.
     * @param user currently authenticated user data.
     * @param servletRequest servlet's request.
     */
    @DeleteMapping("/subject/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final int id, final Principal user, final HttpServletRequest servletRequest)
            throws Exception {
        final Subject subject = this.subjectService.getById(id);
        if (!this.checkAccessToResource(servletRequest, user, subject.getUserKey())) {
            throw new AccessDeniedException("You don't have access to this resource!");
        }
        this.subjectService.delete(id);
    }
}