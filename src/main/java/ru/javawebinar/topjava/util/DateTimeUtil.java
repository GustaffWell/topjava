package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<? super T>> boolean isBetweenHalfOpen(T lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseStartDate(String startDateString) {
        return startDateString == null || startDateString.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDateString);
    }

    public static LocalDate parseEndDate(String endDateString) {
        return endDateString == null || endDateString.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDateString).plusDays(1);
    }

    public static LocalTime parseStartTime(String startTimeString) {
        return startTimeString == null || startTimeString.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTimeString);
    }

    public static LocalTime parseEndTime(String endTimeString) {
        return endTimeString == null || endTimeString.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTimeString);
    }
}

