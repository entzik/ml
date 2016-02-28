package com.thekirschners.ml.data.raw.weather;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by emilkirschner on 24/02/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMetrics {
    final int temperature;
    final int presure;
    final int humidity;
    final int tempMin;
    final int tempMax;

    @JsonCreator
    public WeatherMetrics(
            @JsonProperty("temp") int temperature,
            @JsonProperty("pressure") int presure,
            @JsonProperty("humidity") int humidity,
            @JsonProperty("temp_min") int tempMin,
            @JsonProperty("temp_max") int tempMax) {
        this.temperature = temperature;
        this.presure = presure;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    @JsonProperty("temp")
    public int temperature() {
        return temperature;
    }

    @JsonProperty("pressure")
    public int presure() {
        return presure;
    }

    @JsonProperty("humidity")
    public int humidity() {
        return humidity;
    }

    @JsonProperty("temp_min")
    public int tempMin() {
        return tempMin;
    }

    @JsonProperty("temp_max")
    public int tempMax() {
        return tempMax;
    }
}
