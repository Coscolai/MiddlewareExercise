package com.middleware;

import java.util.Set;

/**
 *  Interface for a PriceStore.
 *
 * @author Kevin Scully
 */
public interface Prices {
    /**
     * Add a price to the store overwriting any older matching examples.
     * @param price
     */
    void addPrice(Price price);

    /**
     * Cache of prices by Instrument.
     * @return
     */
    RequestCache getInstrumentPrices();
    /**
     * Cache of prices by Vendor.
     * @return
     */
    RequestCache getVendorPrices();
    /**
     * Return a Set of Prices matching the Id.
     * @param id
     * @param identifier  Function to identify tag type (Vendor, Instrument)
     * @return Set
     */
    Set<Price> prices(String id, PriceIdentifier identifier);
}
