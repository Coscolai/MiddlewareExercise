package com.middleware;

import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kevin on 27/02/2016.
 */
public class VendorRequestsTest extends RequestCacheTest {


    protected Set<Price> vendor1Set;
    protected Set<Price> vendor2Set;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        vendor1Set = new HashSet<Price>();
        vendor1Set.add(price1);
        vendor1Set.add(price2);
        vendor2Set = new HashSet<Price>();
        vendor2Set.add(price3);
    }

    @Override
    protected void setUpStore() {
        store = context.mock(Prices.class);
        requests = new VendorRequests(store);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        vendor1Set = null;
        vendor2Set = null;
    }

    @Test
    public void testRequestPrices() throws Exception {
        context.checking(new Expectations() {{
            exactly(1).of(store).prices("Vendor1", requests.getIdentifier()); will(returnValue(vendor1Set));
            exactly(1).of(store).prices("Vendor2", requests.getIdentifier()); will(returnValue(vendor2Set));
        }});
        requests.requestPrices("Vendor1");
        requests.requestPrices("Vendor1");
        requests.requestPrices("Vendor2");
        requests.requestPrices("Vendor2");
        context.assertIsSatisfied();
    }

    @Test
    public void testInvalidatePrice() throws Exception {
        context.checking(new Expectations() {{
            exactly(2).of(store).prices("Vendor1", requests.getIdentifier()); will(returnValue(vendor1Set));
            exactly(1).of(store).prices("Vendor2", requests.getIdentifier()); will(returnValue(vendor2Set));
        }});
        requests.requestPrices("Vendor1");
        requests.requestPrices("Vendor2");
        requests.invalidatePrice(price1);
        requests.requestPrices("Vendor1");
        requests.requestPrices("Vendor2");

        context.assertIsSatisfied();
    }

    protected String getIDTag(Price price) {
        return price1.getVendorName();
    }
}