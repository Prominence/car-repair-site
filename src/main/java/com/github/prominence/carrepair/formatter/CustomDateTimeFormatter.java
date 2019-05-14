package com.github.prominence.carrepair.formatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomDateTimeFormatter implements Formatter<LocalDateTime> {
    private static final Logger logger = LogManager.getLogger(CustomDateTimeFormatter.class);
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        if (StringUtils.isEmpty(text)) return null;
        final LocalDateTime parsedDateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
        logger.trace("Parsing String[{}] to LocalDateTime instance: {}.", () -> text, () -> parsedDateTime);
        return parsedDateTime;
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        if (object == null) return null;
        final String formattedString = object.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.trace("Formatting LocalDateTime[{}] to String instance: {}.", () -> object, () -> formattedString);
        return formattedString;
    }
}
