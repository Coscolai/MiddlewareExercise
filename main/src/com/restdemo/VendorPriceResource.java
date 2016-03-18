package com.restdemo;

import com.middleware.Price;
import com.middleware.PriceStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by kevin on 02/03/2016.
 */
@Path("/fetch/vendor")
public class VendorPriceResource extends PriceResource {

    Set<Price> getPrices(@PathParam("priceId") String priceId) {
        return Main.STORE.getVendorPrices().requestPrices(priceId);
    }

}
