package com.middleware;

/**
 * Concrete implementation to cache by Vendor Id.
 *
 * @author Kevin Scully
 */
public class VendorRequests extends RequestCache {

    VendorRequests(Prices priceStore) {
        super(priceStore);
    }

    @Override
    protected PriceIdentifier getIdentifier() {
        return (price) -> price.getVendorName();
    }
}
