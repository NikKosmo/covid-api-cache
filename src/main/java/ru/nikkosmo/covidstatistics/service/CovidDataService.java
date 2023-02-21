package ru.nikkosmo.covidstatistics.service;

import jakarta.inject.Singleton;
import ru.nikkosmo.covidstatistics.api.CovidStatisticsForPeriod;

import java.time.LocalDate;
import java.util.Collection;

@Singleton
public class CovidDataService {

    private final NewCasesByDayStorage newCasesByDayStorage;

    public CovidDataService(NewCasesByDayStorage newCasesByDayStorage) {
        this.newCasesByDayStorage = newCasesByDayStorage;
    }

    public CovidStatisticsForPeriod statisticsForPeriod(LocalDate fromDate, LocalDate toDate) {
        var newCasesByDate = newCasesByDayStorage.getNewCasesForPeriod(fromDate, toDate);
        return extractStatistics(newCasesByDate);
    }

    private CovidStatisticsForPeriod extractStatistics(Collection<Integer> newConfirmedCases) {
        if (newConfirmedCases.isEmpty()) {
            return new CovidStatisticsForPeriod(0, 0);
        }
        int minConfirmedCases = Integer.MAX_VALUE;
        int maxConfirmedCases = 0;
        for (var i : newConfirmedCases) {
            minConfirmedCases = Math.min(i, minConfirmedCases);
            maxConfirmedCases = Math.max(i, maxConfirmedCases);
        }
        return new CovidStatisticsForPeriod(minConfirmedCases, maxConfirmedCases);
    }
}
