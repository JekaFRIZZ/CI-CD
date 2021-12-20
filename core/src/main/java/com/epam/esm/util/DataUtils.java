package com.epam.esm.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataUtils {
    public static LocalDateTime parseLocalDateType(String date, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static LocalDateTime getCurrentTime(String dateTimePattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern, Locale.ENGLISH);
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dateTimeFormatter.format(now);
        return LocalDateTime.parse(currentTime);
    }
}
