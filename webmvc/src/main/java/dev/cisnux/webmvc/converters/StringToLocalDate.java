package dev.cisnux.webmvc.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class StringToLocalDate implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeException dateTimeException) {
            log.warn("failed to parsed " + source + ':', dateTimeException);
            return null;
        }
    }
}
