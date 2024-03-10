package ru.checkdev.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.generator.domain.Exam;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    @GetMapping("/create/{text}")
    public ResponseEntity<Exam> get(@PathVariable String text) {


        return null;


    }
}
