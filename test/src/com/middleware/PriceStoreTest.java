package com.middleware;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kevin on 25/02/2016.
 */
public class PriceStoreTest {
    Price price1;
    Price price2;
    Price price3;

    PriceStore priceStore;

    @Before
    public void setUp() throws Exception {
        price1 = new Price("Vendor1", "Inst1", 123.0);
        price2 = new Price("Vendor1", "Inst2", 456.11);
        price3 = new Price("Vendor2", "Inst1", 124.0);
        priceStore = PriceStore.INSTANCE;
        priceStore.addPrice(price1);
        priceStore.addPrice(price2);
        priceStore.addPrice(price3);
    }

    @After
    public void tearDown() throws Exception {
        price1 = null;
        price2 = null;
        price3 = null;
        priceStore.stopExpirer();
        priceStore = null;
    }

    @Test
    public void testPricesForInstrument() throws Exception {
        // Check we get the right number of prices for each Instrument Id
        assertEquals(priceStore.getInstrumentPrices().requestPrices("Inst1").size(), 2);
        assertEquals(priceStore.getInstrumentPrices().requestPrices("Inst2").size(), 1);
    }

    @Test
    public void testPricesForVendor() throws Exception {
        // Check we get the right number of prices for each Vendor Id
        assertEquals(priceStore.getVendorPrices().requestPrices("Vendor1").size(), 2);
        assertEquals(priceStore.getVendorPrices().requestPrices("Vendor2").size(), 1);
    }

    @Test
    public void testExpirePrices() throws Exception {
        int timeToLive = PriceStore.DAYS_TO_LIVE; //PriceStore has this hardcoded.
        Instant reference = price1.getInstantRecorded(); // This is when the test is run
        Instant oldInstant = reference.minus(timeToLive+3, ChronoUnit.DAYS);
        //Add Older Prices
        Price oldPrice1 = new Price("OldVendor", "OleInst", 123.0, oldInstant);
        Price oldPrice2 = new Price("OldVendor", "OleInst2", 124.0, oldInstant);
        priceStore.addPrice(oldPrice1);
        priceStore.addPrice(oldPrice2);
        assertTrue("Old prices should be present", (priceStore.getVendorPrices().requestPrices("OldVendor")).size() == 2);
        assertTrue("Old prices should be present", (priceStore.getInstrumentPrices().requestPrices("OleInst")).size() == 1);
        priceStore.expirePrices(reference);
        assertTrue("Old prices should not be present", (priceStore.getVendorPrices().requestPrices("OldVendor")).size() == 0);
        assertTrue("Old prices should not be present", (priceStore.getInstrumentPrices().requestPrices("OleInst")).size() == 0);
        assertTrue("Unexpired prices should still be present", (priceStore.getInstrumentPrices().requestPrices("Inst2")).size() == 1);
        assertTrue("Unexpired prices should still be present", (priceStore.getVendorPrices().requestPrices("Vendor1")).size() == 2);
    }
    @Test
    public void testExpirer() throws Exception {
        int timeToLive = 30;
        Instant reference = price1.getInstantRecorded(); // This is when the test is run
        Instant oldInstant = reference.minus(timeToLive+3, ChronoUnit.DAYS);
        //Add Older Prices
        Price oldPrice1 = new Price("OldVendor", "OleInst", 123.0, oldInstant);
        Price oldPrice2 = new Price("OldVendor", "OleInst2", 124.0, oldInstant);
        priceStore.addPrice(oldPrice1);
        priceStore.addPrice(oldPrice2);
        assertTrue("Old prices should be present", (priceStore.getVendorPrices().requestPrices("OldVendor")).size() == 2);
        assertTrue("Old prices should be present", (priceStore.getInstrumentPrices().requestPrices("OleInst")).size() == 1);
        priceStore.startExpirationProcess();
        Thread.currentThread().sleep(10); //Very crude. Give the price expirer a chance to run
        assertTrue("Old prices should not be present", (priceStore.getVendorPrices().requestPrices("OldVendor")).size() == 0);
        assertTrue("Old prices should not be present", (priceStore.getInstrumentPrices().requestPrices("OleInst")).size() == 0);
        assertTrue("Unexpired prices should still be present", (priceStore.getInstrumentPrices().requestPrices("Inst2")).size() == 1);
        assertTrue("Unexpired prices should still be present", (priceStore.getVendorPrices().requestPrices("Vendor1")).size() == 2);
    }

    @Test
    public void testMissingPrices() throws Exception {
        Set missingVendorPrices = priceStore.getVendorPrices().requestPrices("NotAVendor");
        assertTrue("Expected and empty Set", missingVendorPrices.isEmpty());
        Set missingInstrumentPrices = priceStore.getVendorPrices().requestPrices("NotAnInstrument");
        assertTrue("Expected and empty Set", missingVendorPrices.isEmpty());
    }


}