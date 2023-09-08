package ru.checkdev.forum.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.forum.domain.Category;
import ru.checkdev.forum.service.CategoryService;

import java.util.List;

/**
 * Controller performing requests related to categories.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RestController
@RequestMapping("/forum/category/")
public class CategoryController extends BaseController {

    /**
     * Service used to perform actions on categories.
     */
    private final CategoryService categoryService;

    /**
     * Constructs <code>CategoryController</code> object.
     *
     * @param categoryService injected category service object.
     */
    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Handler for list all categories request.
     *
     * @return list of found categories. It is automatically converted to JSON array in response body by Spring MVC.
     */
    @GetMapping
    public List<Category> list() {
        return this.categoryService.getAll();
    }

    /**
     * Handler for request used to get one category by provided id.
     *
     * @param id category's id. Is is automatically extracted from path variable.
     * @return found category. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @GetMapping("/{id}")
    public Category get(@PathVariable final int id) {
        return this.categoryService.getById(id);
    }

    /**
     * Handler for add new category request.
     *
     * @param categoryRequest new category's data automatically created from JSON object in request body.
     * @return added category. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Category add(@RequestBody final Category categoryRequest) {
        if (categoryRequest.getName().isEmpty() || categoryRequest.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }

        final Category category = new Category();
        category.setPosition(categoryRequest.getPosition());
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        return this.categoryService.save(category);
    }

    /**
     * Handler for update category request.
     *
     * @param id category's id. It is automatically extracted from path variable.
     * @param categoryRequest new category's data automatically created from JSON object in request body.
     * @return updated category. It is automatically converted to JSON object in response body by Spring MVC.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(@PathVariable final int id, @RequestBody final Category categoryRequest) {
        if (categoryRequest.getName().isEmpty() || categoryRequest.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Not enough data in request");
        }

        final Category category = this.categoryService.getById(id);
        category.setPosition(categoryRequest.getPosition());
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        return this.categoryService.save(category);
    }

    /**
     * Handler for delete category request.
     *
     * @param id category's id. It is automatically extracted from path variable.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable final int id) {
        this.categoryService.delete(id);
    }
}