package com.thekirschners.ml.data;

import com.thekirschners.ml.data.raw.bikes.jcdecaux.BikeServiceFactory;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.JcdBikeService;
import com.thekirschners.ml.data.raw.weather.WeatherService;
import com.thekirschners.ml.data.raw.weather.WeatherServiceFactory;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class Services {
    public static WeatherService weather() {
        return WeatherServiceFactory.instance().get();
    }

    public static JcdBikeService jcdBikeService() {
        return BikeServiceFactory.instance().get();
    }
}
