package com.thekirschners.ml.data.raw.weather;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class WindConditions {
    final double speed;
    final double orientation;

    @JsonCreator
    public WindConditions(
            @JsonProperty("speed") double speed,
            @JsonProperty("deg") int orientation
    ) {
        this.speed = speed;
        this.orientation = orientation;
    }

    @JsonProperty("speed")
    public double speed() {
        return speed;
    }

    @JsonProperty("deg")
    public double orientation() {
        return orientation;
    }
}
