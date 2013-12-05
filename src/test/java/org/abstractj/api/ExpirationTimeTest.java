package org.abstractj.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpirationTimeTest {

    private ExpirationTime expirationTime;

    @Before
    public void setUp() throws Exception {
        expirationTime = new ExpirationTime(1);
    }

    @Test
    public void testAdd() throws Exception {
        ExpirationTime expirationTime = new ExpirationTime(2);
        assertFalse(expirationTime.isExpired(expirationTime.getCurrentTime()));
    }

    @Test
    public void testIsExpired() throws Exception {
        ExpirationTime expirationTime = new ExpirationTime();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(2009, 11, 10);
        assertTrue(expirationTime.isExpired(calendar.getTimeInMillis()));
    }
}
