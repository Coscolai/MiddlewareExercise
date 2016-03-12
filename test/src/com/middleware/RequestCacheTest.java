package com.middleware;

import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kevin on 27/02/2016.
 */
public abstract class RequestCacheTest {

    protected Mockery context = new Mockery();
    protected Prices store;
    protected RequestCache requests;
    protected Price price1;
    protected Price price2;
    protected Price price3;
    protected Price price4;

    @Before
    public void setUp() throws Exception {
        price1 = new Price("Vendor1", "Inst1", 123.0);
        price2 = new Price("Vendor1", "Inst2", 456.11);
        price3 = new Price("Vendor2", "Inst1", 124.0);
        price4 = new Price("Vendor2", "Inst2", 125.0);
        setUpStore();
    }

    protected abstract void setUpStore();

    @After
    public void tearDown() throws Exception {
        price1 = null;
        price2 = null;
        price3 = null;
        price4 = null;
        requests = null;
        store = null;
    }

    @Test
    public abstract void testRequestPrices() throws Exception;

    @Test
    public abstract void testInvalidatePrice() throws Exception;

    @Test
    public void testGetIdentifier() throws Exception {
        String tag = getIDTag(price1);
        PriceIdentifier identifier = requests.getIdentifier();
        String id = identifier.getPriceKey(price1);
        assertEquals("The id generated from the Lambda function should be the price's vendorname.", tag, id);
    }

    protected abstract String getIDTag(Price price);
}