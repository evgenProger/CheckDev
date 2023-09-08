package ru.checkdev.interview.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.interview.domain.TAlgo;
import ru.checkdev.interview.service.Compile;
import ru.checkdev.interview.service.TAlgoService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RequestMapping("/algo")
@RestController
public class TaskAlgoController {
    private final TAlgoService tasks;

    @Autowired
    public TaskAlgoController(final TAlgoService tasks) {
        this.tasks = tasks;
    }

    @GetMapping("/{taskId}")
    public TAlgo get(@PathVariable int taskId) {
        return this.tasks.findOne(taskId);
    }

    @GetMapping("/")
    public List<TAlgo> list(Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.tasks.findAll();
    }

    @GetMapping("/published")
    public List<TAlgo> listPublished(Principal user) {
        return this.tasks.findByPublished(true);
    }

    @PostMapping("/")
    public TAlgo save(@RequestBody TAlgo task) {
        return this.tasks.save(task);
    }

    @PostMapping("/run")
    public String run(@RequestBody TAlgo task) throws Exception {
        return new Compile().task(
                new Compile.Source(task.getSname(), task.getSource()),
                new Compile.Source(task.getTname(), task.getTest())
        ).getInfo();
    }
}
