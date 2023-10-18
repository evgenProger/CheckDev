package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.notification.dto.UserDTO;
import ru.checkdev.notification.service.UserService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable int id) {
        UserDTO userDTO = service.getUserDTOById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/subscribeCategoryAdd")
    public ResponseEntity<UserDTO> toAddSubscribeCategory(@RequestBody UserDTO userDTO) {
        var created = service.saveSubscribeCategories(userDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/subscribeCategoryDelete")
    public ResponseEntity<UserDTO> toDeleteSubscribeCategory(@RequestBody UserDTO userDTO) {
        var created = service.deleteSubscribeCategories(userDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}