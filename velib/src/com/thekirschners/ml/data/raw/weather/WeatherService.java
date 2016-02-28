package com.thekirschners.ml.data.raw.weather;


import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

/**
 * Created by emilkirschner on 24/02/16.
 */
public interface WeatherService {
    @GET
    public WeatherData now(@QueryParam("appid") String apiKey, @QueryParam("q") String where, @QueryParam("units") String units);
}
