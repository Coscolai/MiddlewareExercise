package com.restdemo;

import com.middleware.Price;
import com.middleware.PriceStore;
import com.middleware.Prices;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Simple REST Server for Demo.
 *
 * @Author  Kevin
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/pricestore/";
    public static final Prices STORE = PriceStore.INSTANCE;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages("com.restdemo");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Add a few basic prices to play with
     */
    public static void initializeSamplePrices() {
        STORE.addPrice(new Price("Vendor1", "Inst1", 25.6));
        STORE.addPrice(new Price("Vendor2", "Inst1", 25.7));
        STORE.addPrice(new Price("Vendor3", "Inst1", 24.2));
        STORE.addPrice(new Price("Vendor1", "Inst2", 10.2));
        STORE.addPrice(new Price("Vendor1", "Inst3", 43.6));
        STORE.addPrice(new Price("Vendor3", "Inst3", 43.3));
    }

    /**
     * Main method.
     * @param args Command Line Args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        initializeSamplePrices();
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdown(); // stop();
    }

}
