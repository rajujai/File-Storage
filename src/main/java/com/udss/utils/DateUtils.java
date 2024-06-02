package com.udss.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {
    public static final ZoneId defaultZoneId = ZoneId.of("Asia/Kolkata");
    public static final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");

    public static String format(final Instant instant, final SimpleDateFormat formatter){
        final ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        final Date date = Date.from(zonedDateTime.toInstant());
        return formatter.format(date);
    }
}
