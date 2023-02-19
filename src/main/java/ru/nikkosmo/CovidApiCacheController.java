package ru.nikkosmo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.time.Instant;

@Controller("/covid-data")
public class CovidApiCacheController {

    private final CovidDataService covidDataService;

    public CovidApiCacheController(CovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @Get("/world")
    public WorldData worldData(Instant from, Instant to) {
        return covidDataService.worldData(from, to);
    }
}