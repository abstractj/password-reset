package org.abstractj.api;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ExpirationTime {

    private final Calendar calendar;

    public ExpirationTime() {
        calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public long getCurrentTime() {
        return calendar.getTimeInMillis();
    }

    public long add(int hours) {
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTimeInMillis();
    }

    public boolean isExpired(long time) {
        return calendar.after(newCalendar(time));
    }

    private Calendar newCalendar(long time) {
        Calendar newCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        newCalendar.setTimeInMillis(time);
        return newCalendar;
    }

}

