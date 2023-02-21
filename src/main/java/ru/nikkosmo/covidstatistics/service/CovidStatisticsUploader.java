package ru.nikkosmo.covidstatistics.service;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Async;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nikkosmo.covidstatistics.covid19api.CovidDataClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Singleton
@Requires(notEnv = Environment.TEST)
public class CovidStatisticsUploader {

    private static final Logger log = LoggerFactory.getLogger(CovidStatisticsUploader.class);

    private final NewCasesByDayStorage newCasesByDayStorage;

    private final CovidDataClient covidDataClient;

    public CovidStatisticsUploader(NewCasesByDayStorage newCasesByDayStorage, CovidDataClient covidDataClient) {
        this.newCasesByDayStorage = newCasesByDayStorage;
        this.covidDataClient = covidDataClient;
    }

    @EventListener
    @Async
    public void uploadExitingData(ServerStartupEvent event) {
        log.info("Started loading initial covid data");

        var startOfCovidYear = LocalDate.ofYearDay(2019, 1).atStartOfDay().toInstant(ZoneOffset.UTC);
        var todayMidnight = Instant.now().truncatedTo(ChronoUnit.DAYS);
        var covidDays = covidDataClient.getDayStatisticsForPeriod(startOfCovidYear, todayMidnight);
        newCasesByDayStorage.addNewCases(covidDays);

        log.info("Finished loading initial covid data");
    }

    @Scheduled(cron = "0 0 * * *", zoneId = "UTC")
    public void uploadDataForYesterday() {
        log.info("Started loading daily covid data");

        var todayMidnight = Instant.now().truncatedTo(ChronoUnit.DAYS);
        var yesterdayMidnight = todayMidnight.minus(1, ChronoUnit.DAYS);
        var covidDays = covidDataClient.getDayStatisticsForPeriod(yesterdayMidnight, todayMidnight);
        newCasesByDayStorage.addNewCases(covidDays);

        log.info("Finished loading daily covid data");
    }
}
