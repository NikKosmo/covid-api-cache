package ru.nikkosmo.covidstatistics.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import ru.nikkosmo.covidstatistics.AbstractTest;
import ru.nikkosmo.covidstatistics.covid19api.CovidDay;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class CovidDataServiceTest extends AbstractTest {

    @Inject
    private CovidDataService covidDataService;

    @Test
    public void returnZeroesIfThereIsNoDataForPeriod() {
        // given
        var today = LocalDate.now();
        var covidDay = getCovidDay(1, today);
        newCasesByDayStorage.addNewCases(List.of(covidDay));

        // when
        var from = today.plusDays(1);
        var to = LocalDate.now().plusDays(1);
        var covidStatisticsForPeriod = covidDataService.statisticsForPeriod(from, to);

        // then
        assertEquals(0, covidStatisticsForPeriod.minConfirmedCases());
        assertEquals(0, covidStatisticsForPeriod.maxConfirmedCases());
    }

    @Test
    public void returnSameValuesIfThereIsOnlyOneDataPointInPeriod() {
        // given
        var today = LocalDate.now();
        int newConfirmedCases = 1;
        var covidDay = getCovidDay(newConfirmedCases, today);
        newCasesByDayStorage.addNewCases(List.of(covidDay));

        // when
        var from = today.minusDays(1);
        var to = LocalDate.now().plusDays(1);
        var covidStatisticsForPeriod = covidDataService.statisticsForPeriod(from, to);

        // then
        assertEquals(newConfirmedCases, covidStatisticsForPeriod.minConfirmedCases());
        assertEquals(newConfirmedCases, covidStatisticsForPeriod.maxConfirmedCases());
    }

    @Test
    public void returnCorrectValuesForMultipleDataPointsInPeriod() {
        // given
        var today = LocalDate.now();
        var daysInPeriod = List.of(
                getCovidDay(1, today),
                getCovidDay(2, today.plusDays(1)),
                getCovidDay(3, today.plusDays(2)));
        newCasesByDayStorage.addNewCases(daysInPeriod);

        // when
        var from = today.minusDays(1);
        var to = LocalDate.now().plusDays(5);
        var covidStatisticsForPeriod = covidDataService.statisticsForPeriod(from, to);

        // then
        var expectedMin = daysInPeriod.stream()
                .map(CovidDay::newConfirmedCases)
                .min(Comparator.naturalOrder())
                .get();
        var expectedMax = daysInPeriod.stream()
                .map(CovidDay::newConfirmedCases)
                .max(Comparator.naturalOrder())
                .get();
        assertEquals(expectedMin, covidStatisticsForPeriod.minConfirmedCases());
        assertEquals(expectedMax, covidStatisticsForPeriod.maxConfirmedCases());
    }
}