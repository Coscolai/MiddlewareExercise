package com.middleware.datastore;

import com.middleware.Price;
import com.middleware.PriceIdentifier;
import com.middleware.RequestCache;

import java.time.Instant;
import java.util.Set;

/**
 * Created by kevin on 14/03/2016.
 */
public interface PriceStoreDAO {
    /**
     * Add a price to the store overwriting any older matching examples.
     * @param price
     */
    void addPrice(Price price);

    void removePrice(Price price);

    /**
     * Return a Set of Prices matching the Id.
     * @param id
     * @param identifier  Function to identify tag type (Vendor, Instrument)
     * @return Set
     */
    Set<Price> prices(String id, PriceIdentifier identifier);

    Set<Price> expirePrices(Instant reference, int daysToLive);

}
