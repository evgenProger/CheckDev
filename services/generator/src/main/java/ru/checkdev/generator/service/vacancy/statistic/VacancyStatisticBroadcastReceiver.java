package ru.checkdev.generator.service.vacancy.statistic;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.checkdev.generator.domain.VacancyStatistic;

@Component
@AllArgsConstructor
public class VacancyStatisticBroadcastReceiver {

    private final VacancyStatisticService<VacancyStatistic, Integer> service;

    @Scheduled(cron = "${scheduled.task.cron}")
    public void runTask() {
        service.saveStatistic(service.renewStatistic());
    }
}
