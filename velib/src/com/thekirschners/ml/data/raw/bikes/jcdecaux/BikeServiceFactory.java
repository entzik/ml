package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

/**
 * Created by emilkirschner on 19/02/16.
 */
public class BikeServiceFactory {
    static final String API_KEY = "77571707f075ff31ce2dc32fa6da4e9a407c9a0e";


    private static final BikeServiceFactory INSTANCE = new BikeServiceFactory();

    public static BikeServiceFactory instance() {
        return INSTANCE;
    }

    private final JcdBikeService bikeService;

    private BikeServiceFactory() {
        ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule myModule = new SimpleModule("myModule", new Version(1, 0, 0, null));
        mapper.registerModule(myModule);
        resteasyJacksonProvider.setMapper(mapper);
        ResteasyClient client = new ResteasyClientBuilder().register(resteasyJacksonProvider).build();
        ResteasyWebTarget target = client.target("https://api.jcdecaux.com/vls/v1/");

        bikeService = target.proxy(JcdBikeService.class);
    }

    public JcdBikeService get() {
        return bikeService;
    }

    public static void main(String[] args) {
        new BikeServiceFactory();
    }
}