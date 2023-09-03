package ru.job4j.template.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.job4j.template.Application;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@RequestMapping("/blog")
@RestController
public class TemplateController {
    @Autowired
    private ApplicationContext ctx;

    @PostMapping("/close")
    public String close() {
        SpringApplication.exit(ctx, () -> 0);
        return "success";
    }


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
