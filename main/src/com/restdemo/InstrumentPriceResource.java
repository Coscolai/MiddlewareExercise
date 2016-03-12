package com.restdemo;

import com.middleware.Price;
import com.middleware.PriceStore;

import javax.ws.rs.*;
import java.util.Set;

/**
 * Created by kevin on 02/03/2016.
 */
@Path("/fetch/instrument")
public class InstrumentPriceResource extends PriceResource {
    Set<Price> getPrices(@PathParam("priceId") String priceId) {
        return Main.STORE.getInstrumentPrices().requestPrices(priceId);
    }
}
