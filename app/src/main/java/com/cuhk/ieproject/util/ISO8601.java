package com.cuhk.ieproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by anson on 16/6/2016.
 */
public class ISO8601 {
    public static Date fromString(String str, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.parse(str);
    }

    public static Date fromString(String str) throws ParseException {
        return fromString(str, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    }

    public static Date fromStringWithoutMilliseconds(String str) throws ParseException {
        return fromString(str, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public static Date fromStringWithoutZ(String str) throws ParseException {
        return fromString(str, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    }

    public static String toString(Date date, String format, TimeZone timezone){
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        df.setTimeZone(timezone);
        return df.format(date);
    }

    public static String toString(Date date, String format) {
        return toString(date, format, TimeZone.getDefault());
    }

    public static String toString(Date date) {
        return toString(date, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    }

    public static String toStringWithoutZ(Date date) {
        return toString(date, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    }

    public static String toStringWithoutMillisecondAndZ(Date date) {
        return toString(date, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String toStringWithoutMillisecondAndZ(Date date, TimeZone timeZone) {
        return toString(date, "yyyy-MM-dd'T'HH:mm:ss", timeZone);
    }

    public static Calendar fromDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar fromStringToCalendar(String str, String format) throws ParseException {
        return fromDateToCalendar(fromString(str, format));
    }

    public static Calendar fromStringToCalendar(String str) throws ParseException {
        return fromDateToCalendar(fromString(str));
    }

    public static Calendar fromStringWithoutZToCalendar(String str) throws ParseException {
        return fromDateToCalendar(fromStringWithoutZ(str));
    }
}
