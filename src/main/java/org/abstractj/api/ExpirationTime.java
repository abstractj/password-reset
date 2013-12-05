package org.abstractj.api;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ExpirationTime {

    private final Calendar calendar;
    private long expirationDate;

    public ExpirationTime() {
        calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public ExpirationTime(int expirationDate) {
        this();
        this.expirationDate = add(expirationDate);
    }

    public long getCurrentTime() {
        return calendar.getTimeInMillis();
    }

    public long add(int hours) {
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTimeInMillis();
    }

    public boolean isExpired(long time) {
        return calendar.after(getCalendar(time));
    }

    private Calendar getCalendar(long time) {
        Calendar comparison = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        comparison.setTimeInMillis(time);
        return comparison;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

}

