package ru.checkdev.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.generator.domain.VacancyStatistic;
import ru.checkdev.generator.dto.DirectionKey;
import ru.checkdev.generator.service.vacancy.statistic.VacancyStatisticService;
import ru.checkdev.generator.service.vacancy.statistic.mapper.StatisticMapper;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final VacancyStatisticService<VacancyStatistic, Integer> vacancyStatisticService;
    private final StatisticMapper<DirectionKey, VacancyStatistic> mapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody DirectionKey directionKey) {
        var name = directionKey.getName();
        vacancyStatisticService.createItem(mapper.map(directionKey));
    }

    @GetMapping("/get")
    public ResponseEntity<List<VacancyStatistic>> getAll() {
        var result = vacancyStatisticService.getStatistic();
        return ResponseEntity.status(result.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).body(result);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody DirectionKey directionKey) {
        vacancyStatisticService.updateItem(mapper.map(directionKey));
    }

    @GetMapping("/renew")
    public ResponseEntity<List<VacancyStatistic>> renew() {
        var result = vacancyStatisticService.renewStatistic();
        vacancyStatisticService.saveStatistic(result);
        return ResponseEntity.status(result.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).body(result);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        vacancyStatisticService.delete(id);
    }
}
