package com.restdemo;

import com.middleware.Price;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by kevin on 02/03/2016.
 */
abstract public class PriceResource {
    @GET
    @Path("/{priceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object[] fetchPrices(@PathParam("priceId") String priceId) {
        Set prices = getPrices(priceId);
        return prices.toArray(new Price[0]);
    }

    abstract Set<Price> getPrices(@PathParam("priceId") String priceId);

}
