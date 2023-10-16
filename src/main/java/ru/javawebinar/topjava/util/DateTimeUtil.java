package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T lt, T start, T end) {
        return lt.compareTo(start) >= 0 && lt.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate analyzeStartDate(LocalDate startDate) {
        return startDate == null ? LocalDate.MIN : startDate;
    }

    public static LocalDate analyzeEndDate(LocalDate endDate) {
        return endDate == null ? LocalDate.MAX : endDate.plusDays(1);
    }

    public static LocalTime analyzeStartTime(LocalTime startTime) {
        return startTime == null ? LocalTime.MIN : startTime;
    }

    public static LocalTime analyzeEndTime(LocalTime endTime) {
        return endTime == null ? LocalTime.MAX : endTime;
    }

    public static LocalDate parseDate(String dateString) {
        return StringUtils.hasLength(dateString) ? LocalDate.parse(dateString) : null;
    }

    public static LocalTime parseTime(String timeString) {
        return StringUtils.hasLength(timeString) ? LocalTime.parse(timeString) : null;
    }
}

