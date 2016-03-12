package com.middleware;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Represents an actual stored Price.
 *
 * Price is immutable. Prices with equal vendorName and instrumentId are treated as equal.
 *
 * @author Kevin Scully
 */
public final class Price {
    private final String vendorName;
    private final String instrumentId;
    private final Double price;
    private final Instant instantRecorded;

    /**
     * Instantiate a fully qualified Price.
     * @param vendorName
     * @param instrumentId
     * @param price
     * @param instantRecorded
     */
    public Price(String vendorName, String instrumentId, Double price, Instant instantRecorded ) {
        this.vendorName = vendorName;
        this.instrumentId = instrumentId;
        this.price = price;
        this.instantRecorded = instantRecorded;
    }

    /**
     * Instantiate a Price with recorded time defaulted to now.
     * @param vendorName
     * @param instrumentId
     * @param price
     */
    public Price(String vendorName, String instrumentId, Double price ) {
        this.vendorName = vendorName;
        this.instrumentId = instrumentId;
        this.price = price;
        this.instantRecorded = Instant.now();
    }

    // Accessors

    public final String getInstrumentId() {
        return instrumentId;
    }

    public final String getVendorName() {
        return vendorName;
    }

    public final Double getPrice() {
        return price;
    }

    public final Instant getInstantRecorded() { return instantRecorded; }

    /**
     * Return a local date converted from an Instant.
     */
    public LocalDate getLocalDate() {
        return getInstantRecorded().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Equals and HashCode are overridden to equate Prices with teh same Vendor and Instrument Ids.

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        return (object instanceof Price)
                && (Objects.equals(((Price) object).instrumentId, instrumentId))
                && (Objects.equals(((Price) object).vendorName, vendorName));
    }

    @Override
    public int hashCode() {
        return instrumentId.hashCode() + vendorName.hashCode();
    }

    @Override
    public String toString() {
        return "Price{" +
                "vendorName='" + vendorName + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", price=" + price +
                ", dateRecorded=" + getLocalDate() +
                '}';
    }

    /**
     * True if the reference instant is more than dayToLive after the instantRecorded
     * of the Price
     *
     * @param referenceInstant Instant we are measuring against, usually today.
     * @param daysToLive How long can the trade be stored before expiring
     * @return boolean
     */
    public boolean isExpired(Instant referenceInstant, int daysToLive) {
        if(referenceInstant.isBefore(this.getInstantRecorded())) return false;
        return referenceInstant.minus(Duration.ofDays(daysToLive)).isAfter(this.getInstantRecorded());
    }

}
