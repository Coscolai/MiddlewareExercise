package com.middleware;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Cache of Price Requests. The decision on what to search the Price Store with
 * is made by evaluating the getIdentifier lambda function. Concrete subclasses supply this.
 * The internal cache is an instance of a ConcurrentHashMap which gives Thread safety.
 *
 * Created by kevin on 27/02/2016.
 */
public abstract class RequestCache {

    private ConcurrentHashMap<String, Set<Price>> cache;
    private Prices prices;

    RequestCache() {
        super();
        cache = new ConcurrentHashMap<>();
    }

    RequestCache(Prices prices) {
        this.prices = prices;
        cache = new ConcurrentHashMap<>();
    }

    /**
     * Function to return price aspect to search on.
     *
     * @return
     */
    protected abstract PriceIdentifier getIdentifier();

    /**
     * Does teh actual worrk to fetch a price from Store and Cache it.
     * @param priceId
     * @return
     */
    private Set<Price> fetchAndCachePriceFor(String priceId) {
        Set<Price> requestPrices = prices.prices(priceId, getIdentifier());
        if(!requestPrices.isEmpty()) cache.put(priceId, requestPrices);
        return requestPrices;
    }

    /**
     * Return cached prices for Id or request from Price Store and cache them is absent.
     *
     * @param priceId
     * @return Set of Prices
     */
    public Set<Price> requestPrices(String priceId) {
        Set<Price> price = cache.get(priceId);
        if(price == null) {
            price = fetchAndCachePriceFor(priceId);
        }
        return price;
    }

    /**
     * Invalidate (remove) supplied Price.
     * @param price
     */
    public void invalidatePrice(Price price) {
        cache.remove(getIdentifier().getPriceKey(price));
    }

}
