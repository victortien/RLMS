package org.rlms.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
    }

    public static String getDateString(Date date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return ldt.format(formatter);
    }

    public static Date getDate(String dateString, String format) {
        ZonedDateTime zdt = getDateTime(dateString, format);
        return Date.from(zdt.toInstant());
    }

    private static ZonedDateTime getDateTime(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if(isDateTimeFormat(format))
            return LocalDateTime.parse(dateString, formatter).atZone(ZoneId.systemDefault());
        return LocalDate.parse(dateString, formatter).atStartOfDay(ZoneId.systemDefault());
    }

    private static boolean isDateTimeFormat(String format) {
        return DateTimeFormatSet.getDateTimeFormat().stream().anyMatch(e -> e.getFormat().equals(format));
    }

    public static String getDateString(LocalDate localDate, String format) {
        return getDateString(asDate(localDate), format);
    }

    public static String getDateString(LocalDateTime localDateTime, String format) {
        return getDateString(asDate(localDateTime), format);
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
