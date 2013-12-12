package org.abstractj.api;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ExpirationTime {

    private Calendar current;
    private Calendar future;


    public ExpirationTime() {
        current = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        future = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public long getCurrentTime() {
        return current.getTimeInMillis();
    }

    public long add(int hours) {
        future.add(Calendar.HOUR_OF_DAY, hours);
        return future.getTimeInMillis();
    }

    public boolean isExpired(long time) {
        future.setTimeInMillis(time);
        return current.after(future);
    }

}

