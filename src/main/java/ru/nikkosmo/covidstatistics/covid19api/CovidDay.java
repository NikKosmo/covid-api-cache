package ru.nikkosmo.covidstatistics.covid19api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CovidDay(

        @JsonProperty("NewConfirmed")
        int newConfirmedCases,

        @JsonProperty("Date")
        Instant date
) {
}
