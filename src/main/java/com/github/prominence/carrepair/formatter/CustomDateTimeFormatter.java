package com.github.prominence.carrepair.formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomDateTimeFormatter implements Formatter<LocalDateTime> {
    private static final Logger logger = LogManager.getLogger(CustomDateTimeFormatter.class);

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        final LocalDateTime parsedDateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.trace("Parsing String[{}] to LocalDateTime instance: {}.", () -> text, () -> parsedDateTime);
        return parsedDateTime;
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        final String formattedString = object.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.trace("Formatting LocalDateTime[{}] to String instance: {}.", () -> object, () -> formattedString);
        return formattedString;
    }
}
