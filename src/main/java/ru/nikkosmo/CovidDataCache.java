package ru.nikkosmo;

import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Singleton
public class CovidDataCache {

    private final NavigableMap<Instant, Integer> newCasesByDate = new ConcurrentSkipListMap<>();

    public NavigableMap<Instant, Integer> getNewCasesByDate(Instant startDate, Instant endDate) {
        return newCasesByDate.subMap(startDate, true, endDate, true);
    }

    public void put(Instant date, int newCases){
        newCasesByDate.put(date, newCases);
    }
}
