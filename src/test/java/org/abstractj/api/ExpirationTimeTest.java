package org.abstractj.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpirationTimeTest {

    private ExpirationTime expirationTime;

    @Before
    public void setUp() throws Exception {
        expirationTime = new ExpirationTime();
    }

    @Test
    public void testTimeStillValid() throws Exception {
        long time = expirationTime.add(1);
        assertFalse("One hour should be added", expirationTime.isExpired(time));
    }

    @Test
    public void testTimeHasExpired() throws Exception {
        long time = expirationTime.add(-7);
        assertTrue("One hour should be added", expirationTime.isExpired(time));
    }
}
