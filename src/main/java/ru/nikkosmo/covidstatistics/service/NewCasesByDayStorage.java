package ru.nikkosmo.covidstatistics.service;

import jakarta.inject.Singleton;
import ru.nikkosmo.covidstatistics.covid19api.CovidDay;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Singleton
public class NewCasesByDayStorage {

    private final NavigableMap<LocalDate, Integer> newCasesByDate = new ConcurrentSkipListMap<>();

    public Collection<Integer> getNewCasesForPeriod(LocalDate startDate, LocalDate endDate) {
        return newCasesByDate.subMap(startDate, endDate).values();
    }

    public void addNewCases(Collection<CovidDay> covidDays) {
        covidDays.forEach(covidDay ->
                newCasesByDate.put(
                        covidDay.date().atOffset(ZoneOffset.UTC).toLocalDate(),
                        covidDay.newConfirmedCases()));
    }

    // for clearing storage between tests
    public void clearStorage() {
        newCasesByDate.clear();
    }
}
