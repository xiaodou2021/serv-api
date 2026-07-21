package com.iy.api.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class CustomDateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String DATETIME_FORMAT_CHINESE = "yyyy年MM月dd日 HH时mm分ss秒";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    private CustomDateUtils() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }

    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : null;
    }

    public static String formatDate(LocalDate date, String pattern) {
        return date != null ? date.format(DateTimeFormatter.ofPattern(pattern)) : null;
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern(pattern)) : null;
    }

    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT);
    }

    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DATETIME_FORMAT);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime getDayStart(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    public static LocalDateTime getDayEnd(LocalDate date) {
        return date != null ? date.atTime(LocalTime.MAX) : null;
    }

    public static LocalDateTime getDayStart(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atStartOfDay() : null;
    }

    public static LocalDateTime getDayEnd(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atTime(LocalTime.MAX) : null;
    }

    public static LocalDate getWeekStart(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }

    public static LocalDate getWeekEnd(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.plusDays(7 - date.getDayOfWeek().getValue());
    }

    public static LocalDate getMonthStart(LocalDate date) {
        return date != null ? date.withDayOfMonth(1) : null;
    }

    public static LocalDate getMonthEnd(LocalDate date) {
        return date != null ? date.withDayOfMonth(date.lengthOfMonth()) : null;
    }

    public static LocalDate getYearStart(LocalDate date) {
        return date != null ? date.withDayOfYear(1) : null;
    }

    public static LocalDate getYearEnd(LocalDate date) {
        return date != null ? date.withMonth(12).withDayOfMonth(31) : null;
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime != null ? dateTime.plusDays(days) : null;
    }

    public static LocalDate plusDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }

    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime != null ? dateTime.plusMinutes(minutes) : null;
    }

    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime != null ? dateTime.minusDays(days) : null;
    }

    public static LocalDate minusDays(LocalDate date, long days) {
        return date != null ? date.minusDays(days) : null;
    }

    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.minusHours(hours) : null;
    }

    public static int compare(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    public static int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            return 0;
        }
        if (dateTime1 == null) {
            return -1;
        }
        if (dateTime2 == null) {
            return 1;
        }
        return dateTime1.compareTo(dateTime2);
    }

    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isBefore(date2);
    }

    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1 != null && dateTime2 != null && dateTime1.isBefore(dateTime2);
    }

    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isAfter(date2);
    }

    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1 != null && dateTime2 != null && dateTime1.isAfter(dateTime2);
    }

    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isEqual(date2);
    }

    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1 != null && dateTime2 != null && dateTime1.isEqual(dateTime2);
    }

    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public static boolean isLeapYear(LocalDate date) {
        return date != null && date.isLeapYear();
    }

    public static String getDayOfWeek(LocalDate date) {
        if (date == null) {
            return null;
        }
        Map<Integer, String> weekDays = new HashMap<>();
        weekDays.put(1, "星期一");
        weekDays.put(2, "星期二");
        weekDays.put(3, "星期三");
        weekDays.put(4, "星期四");
        weekDays.put(5, "星期五");
        weekDays.put(6, "星期六");
        weekDays.put(7, "星期日");
        return weekDays.get(date.getDayOfWeek().getValue());
    }

    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }

    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    public static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }

    public static int getDaysInMonth(LocalDate date) {
        return date != null ? date.lengthOfMonth() : 0;
    }

    public static int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    public static int getDaysInYear(LocalDate date) {
        return date != null ? (date.isLeapYear() ? 366 : 365) : 0;
    }
}