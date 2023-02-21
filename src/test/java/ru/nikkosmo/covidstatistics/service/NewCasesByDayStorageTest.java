package ru.nikkosmo.covidstatistics.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import ru.nikkosmo.covidstatistics.AbstractTest;
import ru.nikkosmo.covidstatistics.covid19api.CovidDay;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class NewCasesByDayStorageTest extends AbstractTest {

    @Test
    public void returnEmptyResultIfThereIsNoDataInPeriod() {
        // given
        var today = LocalDate.now();
        var covidDay = getCovidDay(1, today);
        newCasesByDayStorage.addNewCases(List.of(covidDay));

        // when
        var from = today.plusDays(1);
        var to = LocalDate.now().plusDays(1);
        var newCasesForPeriod = newCasesByDayStorage.getNewCasesForPeriod(from, to);

        // then
        assertTrue(newCasesForPeriod.isEmpty());
    }

    @Test
    public void returnEmptyResultIfPeriodEndsOnSameDay() {
        // given
        var today = LocalDate.now();
        var covidDay = getCovidDay(1, today);
        newCasesByDayStorage.addNewCases(List.of(covidDay));

        // when
        var from = today.minusDays(1);
        var newCasesForPeriod = newCasesByDayStorage.getNewCasesForPeriod(from, today);

        // then
        assertTrue(newCasesForPeriod.isEmpty());
    }

    @Test
    public void returnCasesForDayIfPeriodStartsOnSameDay() {
        // given
        var today = LocalDate.now();
        var covidDay = getCovidDay(1, today);
        var covidDays = List.of(covidDay);
        newCasesByDayStorage.addNewCases(covidDays);

        // when
        var to = today.plusDays(1);
        var newCasesForPeriod = newCasesByDayStorage.getNewCasesForPeriod(today, to);

        // then
        assertEquals(covidDays.size(), newCasesForPeriod.size());
        assertEquals(covidDay.newConfirmedCases(), newCasesForPeriod.iterator().next());
    }

    @Test
    public void returnSingleCasesForDayIfItWasAddedMultipleTimes() {
        // given
        var today = LocalDate.now();
        var covidDay = getCovidDay(1, today);
        var covidDays = List.of(covidDay, covidDay, covidDay);
        newCasesByDayStorage.addNewCases(covidDays);

        // when
        var to = today.plusDays(1);
        var newCasesForPeriod = newCasesByDayStorage.getNewCasesForPeriod(today, to);

        // then
        assertEquals(1, newCasesForPeriod.size());
        assertEquals(covidDay.newConfirmedCases(), newCasesForPeriod.iterator().next());
    }

    @Test
    public void returnOnlyCasesFoSpecifiedPeriod() {
        // given
        var startOfThePeriod = LocalDate.now();
        var endOfThePeriod = startOfThePeriod.plusDays(5);
        var daysInPeriod = List.of(
                getCovidDay(1, startOfThePeriod),
                getCovidDay(2, startOfThePeriod.plusDays(1)),
                getCovidDay(3, startOfThePeriod.plusDays(2)));
        var daysNotInPeriod = List.of(
                getCovidDay(4, startOfThePeriod.minusDays(1)),
                getCovidDay(5, startOfThePeriod.minusDays(2)));
        newCasesByDayStorage.addNewCases(daysInPeriod);
        newCasesByDayStorage.addNewCases(daysNotInPeriod);

        // when
        var to = startOfThePeriod.plusDays(1);
        var newCasesForPeriod = newCasesByDayStorage.getNewCasesForPeriod(startOfThePeriod, endOfThePeriod);

        // then
        assertEquals(daysInPeriod.size(), newCasesForPeriod.size());
        var expectedCases = daysInPeriod.stream()
                .map(CovidDay::newConfirmedCases)
                .collect(Collectors.toSet());
        assertEquals(expectedCases, new HashSet<>(newCasesForPeriod));
    }
}