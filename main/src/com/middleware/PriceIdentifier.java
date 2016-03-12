package com.middleware;

/**
 * This functional interface is used to type the lambda function that extracts the tag to search on
 * from a Price.
 *
 * @author Kevin Scully
 */
@FunctionalInterface
public interface PriceIdentifier {
    String getPriceKey(Price price);
}
