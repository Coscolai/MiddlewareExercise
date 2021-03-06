package com.restdemo;

import com.middleware.Price;
import com.middleware.PriceStore;

import javax.ws.rs.*;
import java.util.Set;

/**
 * This is a change
 * Created by kevin on 02/03/2016.
 */
@Path("/store")
public class AddPriceResource {
    @PUT
    @Path("/{InstId}")
    public void storePrice(@PathParam("InstId") String instId, @MatrixParam("vendorId") String vendorId, @MatrixParam("price") String price) {
        Price newPrice = new Price(vendorId, instId, new Double(price));
        Main.STORE.addPrice(newPrice);
    }
}
