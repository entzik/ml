package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by emilkirschner on 21/02/16.
 */
public class Coordinates {
    private final double latitude;
    private final double longitude;

    @JsonCreator
    public Coordinates(@JsonProperty("lat") double latitude, @JsonProperty("lng") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @JsonProperty("lat")
    public double latitude() {
        return latitude;
    }

    @JsonProperty("lng")
    public double longitude() {
        return longitude;
    }
}
