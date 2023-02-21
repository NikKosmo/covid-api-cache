package ru.nikkosmo.covidstatistics.covid19api;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

import java.time.Instant;
import java.util.List;

@Client(id = "covid-19-api")
public interface CovidDataClient {


    @Get("/world")
    List<CovidDay> getDayStatisticsForPeriod(@QueryValue Instant from, @QueryValue Instant to);
}
