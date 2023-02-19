package ru.nikkosmo;

import jakarta.inject.Singleton;
import ru.nikkosmo.CovidDataCache;
import ru.nikkosmo.WorldData;
import ru.nikkosmo.covid19api.CovidDataClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

@Singleton
public class CovidDataService {

    private final CovidDataCache covidDataCache;

    private final CovidDataClient covidDataClient;

    public CovidDataService(CovidDataCache covidDataCache, CovidDataClient covidDataClient) {
        this.covidDataCache = covidDataCache;
        this.covidDataClient = covidDataClient;
    }

    public WorldData worldData(Instant from, Instant to) {
        var newCasesByDate = covidDataCache.getNewCasesByDate(from, to);
        if (newCasesByDate.size() < Duration.between(from, to).toDays()) {
            var worldData = covidDataClient.getWorldData(from, to);
            worldData.forEach(covidDay ->
                    covidDataCache.put(covidDay.date(), covidDay.newConfirmedCases()));
            return extractWorldData(covidDataCache.getNewCasesByDate(from, to).values());
        }
        return extractWorldData(newCasesByDate.values());
    }

    private WorldData extractWorldData(Collection<Integer> newConfirmedCases) {
        int minConfirmedCases = Integer.MAX_VALUE;
        int maxConfirmedCases = 0;
        for (Integer i : newConfirmedCases) {
            minConfirmedCases = Math.min(i, minConfirmedCases);
            maxConfirmedCases = Math.max(i, maxConfirmedCases);
        }
        return new WorldData(minConfirmedCases, maxConfirmedCases);
    }
}
