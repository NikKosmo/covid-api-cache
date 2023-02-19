package ru.nikkosmo;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.Optional;

@Singleton
public class InstantConverter implements TypeConverter<Instant, String> {

    @Override
    public Optional<String> convert(Instant inputInstant, Class<String> targetType, ConversionContext context) {
        return Optional.ofNullable(inputInstant)
                .map(Instant::toString);
    }
}