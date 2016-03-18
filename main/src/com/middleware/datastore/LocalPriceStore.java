package com.middleware.datastore;

import com.middleware.Price;
import com.middleware.PriceIdentifier;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by kevin on 14/03/2016.
 */
public class LocalPriceStore implements PriceStoreDAO {

    private Set<Price> prices = (new ConcurrentHashMap<Price, Object>()).newKeySet();

    /**
     * Add a price to the store overwriting any older matching examples.
     *
     * @param price
     */
    @Override
    public void addPrice(Price price) {
        prices.remove(price);
        prices.add(price);
    }

    @Override
    public void removePrice(Price price) {
        prices.remove(price);
    }

    /**
     * Return a Set of Prices matching the Id.
     *
     * @param id
     * @param identifier Function to identify tag type (Vendor, Instrument)
     * @return Set
     */
    @Override
    public Set<Price> prices(String id, PriceIdentifier identifier) {
        Set<Price> prices = this.prices.stream()
                .filter(price -> Objects.equals(identifier.getPriceKey(price), id))
                .collect(Collectors.toSet());
        return prices;
    }

    public Set<Price> expirePrices(Instant reference, int daysToLive) {
        Set<Price> expiredPrices = this.prices.stream()
                .filter(price -> price.isExpired(reference, daysToLive))
                .collect(Collectors.toSet());
        this.prices.removeAll(expiredPrices);
        return expiredPrices;
    }
}
