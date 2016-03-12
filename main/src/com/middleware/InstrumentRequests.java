package com.middleware;

/**
 * Concrete implementation to cache by Instrument Id.
 *
 * @author Kevin Scully
 */
public class InstrumentRequests extends RequestCache {

    InstrumentRequests(Prices prices) {
        super(prices);
    }

    @Override
    protected PriceIdentifier getIdentifier() {
        return (price) -> price.getInstrumentId();
    }
}
