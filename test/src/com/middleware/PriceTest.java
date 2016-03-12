package com.middleware;

import org.junit.After;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

/**
 * Created by kevin on 25/02/2016.
 */
public class PriceTest {
    Price price1;
    Price price2;
    Price price3;

    @org.junit.Before
    public void setUp() throws Exception {
        price1 = new Price("Vendor1", "Inst1", 123.0);
        price2 = new Price("Vendor1", "Inst1", 456.11);
        price3 = new Price("Vendor2", "Inst1", 457.00);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals("Price1 and Price2 should be equal", price1, price2);
        assertNotEquals("Price1 and Price3 should not be equal", price1, price3);
        assertNotEquals("Price and null must not be equal", price1, null);
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals("Price 1 and Price2 hashcodes should be the same.", price1.hashCode(), price2.hashCode());
        assertNotEquals("Price1 and Price3 hashcodes should not be the same.", price1.hashCode(), price3.hashCode());
        assertNotEquals("Price2 and Price3 hashcodes should not be the same.", price2.hashCode(), price3.hashCode());
    }

    @Test
    public void testIsExpired() throws Exception {

        Instant recordedInstant = Instant.parse("2016-01-14T00:00:00Z");
        Instant referenceInstant = Instant.parse("2016-02-15T00:00:00Z");

        Price price = new Price("Vendor", "Instrument", 123.0, recordedInstant);

        boolean expired =  price.isExpired(referenceInstant, 2);
        assertTrue("Price is older that 2 days before reference. Should be expired.", expired);
        expired =  price.isExpired(referenceInstant,34);
        assertFalse("Price isn't 34 days old wrt to reference. Should not be expired.", expired);
    }
}