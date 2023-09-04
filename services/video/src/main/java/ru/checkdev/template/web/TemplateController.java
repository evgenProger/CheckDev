package ru.checkdev.template.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@RequestMapping("/blog/")
@RestController
public class TemplateController {
    @PostMapping("/")
    @PreAuthorize("hasRole('CREATE_ARTICLE')")
    public String createResource() {
        return "protect resource";
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('UPDATE_ARTICLE')")
    public String updateResource() {
        return "protect resource";
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('DELETE_ARTICLE')")
    public String deleteResource() {
        return "protect resource";
    }

    @GetMapping("/")
    public String readResource(@RequestParam(required = false) int page) {
        return "free resource";
    }
}
