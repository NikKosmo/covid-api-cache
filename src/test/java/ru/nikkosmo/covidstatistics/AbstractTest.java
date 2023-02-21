package ru.nikkosmo.covidstatistics;

import io.micronaut.test.annotation.MockBean;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import ru.nikkosmo.covidstatistics.covid19api.CovidDataClient;
import ru.nikkosmo.covidstatistics.covid19api.CovidDay;
import ru.nikkosmo.covidstatistics.service.NewCasesByDayStorage;

import java.time.LocalDate;
import java.time.ZoneOffset;

public abstract class AbstractTest {

    @MockBean
    public CovidDataClient covidDataClient;

    @Inject
    public NewCasesByDayStorage newCasesByDayStorage;

    @BeforeEach
    public void beforeEach() {
        newCasesByDayStorage.clearStorage();
    }

    protected static CovidDay getCovidDay(int newConfirmedCases, LocalDate date) {
        return new CovidDay(newConfirmedCases, date.atTime(12, 0).toInstant(ZoneOffset.UTC));
    }
}
