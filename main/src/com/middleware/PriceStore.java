package com.middleware;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Concrete implementation of an in memory Price Store. It manages the in memory set
 * of received Prices.
 * <p>
 * This class uses Java enum to implement the Singleton pattern. This is purely for convenience.
 * <p>
 * This class stores the Prices in a thread safe concurrent set. This is represented by
 * the key set from a CocurrentHashMap.
 * <p>
 * There are two request caches. For requests via Vendor Id and via Instrument Id. For thread
 * safety these are implemented by ConcurrentHashMaps. Entries for a particular id are cleared
 * (invalidated) by any update to the Price Store for that Id.
 * <p>
 * A scheduled expirer thread removes any expired prices and runs at daily intervals.
 *
 * @author Kevin Scully
 */
public enum PriceStore implements Prices {

    // Single enum representing the Singleton instance.
    INSTANCE;

    static int DAYS_TO_LIVE = 30; //default

    private Set<Price> prices = (new ConcurrentHashMap<Price, Object>()).newKeySet();
    private VendorRequests vendorPrices = new VendorRequests(this);
    private InstrumentRequests instrumentPrices = new InstrumentRequests(this);
    private ScheduledFuture expirer;

    /**
     * Add a price to the store overwriting any older matching examples.
     *
     * @param price
     */
    public void addPrice(Price price) {
        prices.remove(price);
        prices.add(price);
        vendorPrices.invalidatePrice(price);
        instrumentPrices.invalidatePrice(price);
    }

    /**
     * Return a Set of Prices matching the Id.
     *
     * @param id
     * @param identifier Function to identify tag type (Vendor, Instrument)
     * @return Set
     */
    public Set<Price> prices(String id, PriceIdentifier identifier) {
        Set<Price> prices = this.prices.stream()
                .filter(price -> Objects.equals(identifier.getPriceKey(price), id))
                .collect(Collectors.toSet());
        return prices;
    }

    /**
     * Expire all prices daystoLive older than the reference instant
     *
     * @param reference Reference Instant (normally now)
     */
    protected void expirePrices(Instant reference) {
        Set<Price> expiredPrices = this.prices.stream()
                .filter(price -> price.isExpired(reference, DAYS_TO_LIVE))
                .collect(Collectors.toSet());
        this.prices.removeAll(expiredPrices);
        for (Price price : expiredPrices) {
            instrumentPrices.invalidatePrice(price);
            vendorPrices.invalidatePrice(price);
        }
    }

    /**
     * Start and schedule the expirer process.
     *
     * @return A Future used to manage the process
     */
    public ScheduledFuture<?> startExpirationProcess() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable priceExpirer = new Runnable() {
            public void run() {
                expirePrices(Instant.now());
            }
        };
        expirer = scheduler.scheduleAtFixedRate(priceExpirer, 0, 1, TimeUnit.DAYS);
        return expirer;
    }

    public void stopExpirer() {
        if(expirer != null) expirer.cancel(true);
    }

    // Accessors

    public RequestCache getInstrumentPrices() {
        return instrumentPrices;
    }

    public RequestCache getVendorPrices() {
        return vendorPrices;
    }

}