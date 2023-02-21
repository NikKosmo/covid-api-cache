package ru.nikkosmo.covidstatistics;


import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import ru.nikkosmo.covidstatistics.api.CovidStatisticsController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class CovidStatisticsControllerTest extends AbstractTest {

    @Inject
    private CovidStatisticsController covidStatisticsClient;

    @Test
    public void returnStatisticsForSpecifiedPeriod() {
        // when
        var covidStatisticsForPeriod = covidStatisticsClient.statisticsForPeriod(LocalDate.now(), LocalDate.now());

        // then
        assertNotNull(covidStatisticsForPeriod);
    }

    @Test
    public void throwIllegalArgumentIfDatesInverted() {
        // then
        assertThrows(IllegalArgumentException.class,
                () -> covidStatisticsClient.statisticsForPeriod(LocalDate.now(),
                        LocalDate.now().minusDays(1)));
    }
}