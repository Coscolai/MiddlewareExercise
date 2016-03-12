package com.middleware;

import org.jmock.Expectations;
import org.junit.After;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kevin on 28/02/2016.
 */
public class InstrumentRequestsTest extends RequestCacheTest {

    protected Set<Price> instrument1Set;
    protected Set<Price> instrument2Set;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        instrument1Set = new HashSet<Price>();
        instrument1Set.add(price1);
        instrument1Set.add(price3);
        instrument2Set = new HashSet<Price>();
        instrument2Set.add(price2);
        instrument2Set.add(price4);
    }

    @Override
    protected void setUpStore() {
        store = context.mock(Prices.class);
        requests = new InstrumentRequests(store);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        instrument1Set = null;
        instrument2Set = null;
    }


    @Override
    public void testRequestPrices() throws Exception {
        context.checking(new Expectations() {{
            exactly(1).of(store).prices("Inst1", requests.getIdentifier()); will(returnValue(instrument1Set));
            exactly(1).of(store).prices("Inst2", requests.getIdentifier()); will(returnValue(instrument2Set));
        }});
        requests.requestPrices("Inst1");
        requests.requestPrices("Inst1");
        requests.requestPrices("Inst2");
        requests.requestPrices("Inst2");
        context.assertIsSatisfied();
    }

    @Override
    public void testInvalidatePrice() throws Exception {
        context.checking(new Expectations() {{
            exactly(2).of(store).prices("Inst1", requests.getIdentifier()); will(returnValue(instrument1Set));
            exactly(1).of(store).prices("Inst2", requests.getIdentifier()); will(returnValue(instrument2Set));
        }});
        requests.requestPrices("Inst1");
        requests.requestPrices("Inst2");
        requests.invalidatePrice(price1);
        requests.requestPrices("Inst1");
        requests.requestPrices("Inst2");

        context.assertIsSatisfied();    }

    @Override
    protected String getIDTag(Price price) {
        return price1.getInstrumentId();
    }
}
