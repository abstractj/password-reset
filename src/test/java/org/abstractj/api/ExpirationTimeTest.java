package org.abstractj.api;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpirationTimeTest {

    @Test
    public void testTimeStillValid() throws Exception {
        ExpirationTime expirationTime = new ExpirationTime();
        long time = expirationTime.add(1);
        assertFalse("One hour should be added", expirationTime.isExpired(time));
    }

    @Test
    public void testTimeHasExpired() throws Exception {
        ExpirationTime expirationTime = new ExpirationTime();
        long time = expirationTime.add(-7);
        assertTrue("One hour should be added", expirationTime.isExpired(time));
    }
}
