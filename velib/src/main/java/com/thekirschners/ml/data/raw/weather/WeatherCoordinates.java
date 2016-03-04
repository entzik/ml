package com.thekirschners.ml.data.raw.weather;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by emilkirschner on 24/02/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCoordinates {
    private double longitude;
    private double latitude;

    @JsonCreator
    public WeatherCoordinates(
            @JsonProperty("lon") double longitude,
            @JsonProperty("lat") double latitude
    ) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @JsonProperty("lon")
    public double longitude() {
        return longitude;
    }

    @JsonProperty("lat")
    public double latitude() {
        return latitude;
    }
}
