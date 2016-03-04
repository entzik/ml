package com.thekirschners.ml.data.raw.weather;

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
public class WeatherServiceFactory {
    private static final WeatherServiceFactory INSTANCE = new WeatherServiceFactory();

    public static WeatherServiceFactory instance() {
        return INSTANCE;
    }

    private final WeatherService weatherService;

    private WeatherServiceFactory() {
        ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule myModule = new SimpleModule("weatherModule", new Version(1, 0, 0, null));
        mapper.registerModule(myModule);
        resteasyJacksonProvider.setMapper(mapper);
        ResteasyClient client = new ResteasyClientBuilder().register(resteasyJacksonProvider).build();
        ResteasyWebTarget target = client.target("http://api.openweathermap.org/data/2.5/weather");

        weatherService = target.proxy(WeatherService.class);
    }

    public WeatherService get() {
        return weatherService;
    }
}