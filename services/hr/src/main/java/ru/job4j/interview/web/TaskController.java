package ru.job4j.interview.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.job4j.interview.domain.IValue;
import ru.job4j.interview.domain.TAlgo;
import ru.job4j.interview.domain.Task;
import ru.job4j.interview.repository.TAlgoRepository;
import ru.job4j.interview.service.TaskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
*
 * @author parsentev
 * @since 26.09.2016
 */
@RequestMapping("/task")
@RestController
public class TaskController {

    private final TaskService tasks;
    private final TAlgoRepository algos;

    @Autowired
    public TaskController(final TaskService tasks, final TAlgoRepository algos) {
        this.tasks = tasks;
        this.algos = algos;
    }

    @GetMapping("/algo/{ivalueId}")
    public ResponseEntity algo(@PathVariable int ivalueId) {
        IValue value = this.tasks.findIValue(ivalueId);
        TAlgo algo = this.algos.findById(
                Integer.valueOf(value.getTask().getTask().getValues().iterator().next().getValue())
        ).get();
        Map<String, Object> result = new HashMap<>();
        result.put("value", value);
        result.put("algo", algo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public Task get(@PathVariable int taskId) {
        return this.tasks.findOne(taskId);
    }

    @PostMapping("/")
    public Task create(@RequestBody Task task) {
        return this.tasks.create(task);
    }

    @PutMapping("/")
    public Task update(@RequestBody Task task) {
        return this.tasks.update(task);
    }

    @DeleteMapping("/")
    public boolean delete(@RequestParam int id, Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Task task = this.tasks.findOne(id);
        if (task.getVacancy().getKey().equals(key)) {
            return this.tasks.delete(id);
        } else {
            throw new IllegalAccessException("Don't have access");
        }
    }

    @PostMapping("/change/{currentId}/{nextId}")
    public boolean changePosition(@PathVariable int currentId, @PathVariable int nextId, Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        this.tasks.changePosition(currentId, nextId, key);
        return true;
    }
}
