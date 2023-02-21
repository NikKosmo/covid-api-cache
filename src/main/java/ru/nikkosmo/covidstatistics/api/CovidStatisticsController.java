package ru.nikkosmo.covidstatistics.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import ru.nikkosmo.covidstatistics.service.CovidDataService;

import java.time.LocalDate;

@Controller
public class CovidStatisticsController {

    private final CovidDataService covidDataService;

    public CovidStatisticsController(CovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @Get("/statistics")
    public CovidStatisticsForPeriod statisticsForPeriod(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'To' %s is before 'from' %s".formatted(to, from));
        }
        return covidDataService.statisticsForPeriod(from, to);
    }
}