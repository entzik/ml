package com.thekirschners.ml.data.raw.weather;


import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    final WeatherCoordinates coordinates;
    final WeatherCondition[] condition;
    final WeatherMetrics metrics;
    final int visibility;
    final WindConditions wind;
    final long measurementTime;

    @JsonCreator
    public WeatherData(
            @JsonProperty("coord") WeatherCoordinates coordinates,
            @JsonProperty("weather") WeatherCondition[] condition,
            @JsonProperty("main") WeatherMetrics metrics,
            @JsonProperty("visibility") int visibility,
            @JsonProperty("wind") WindConditions wind,
            @JsonProperty("dt") long measurementTime
    ) {
        this.coordinates = coordinates;
        this.condition = condition;
        this.metrics = metrics;
        this.visibility = visibility;
        this.wind = wind;
        this.measurementTime = measurementTime;
    }

    @JsonProperty("coord")
    public WeatherCoordinates coordinates() {
        return coordinates;
    }

    @JsonProperty("weather")
    public WeatherCondition[] condition() {
        return condition;
    }

    @JsonProperty("main")
    public WeatherMetrics metrics() {
        return metrics;
    }

    @JsonProperty("visibility")
    public int visibility() {
        return visibility;
    }

    @JsonProperty("wind")
    public WindConditions wind() {
        return wind;
    }

    @JsonProperty("dt")
    public long measurementTime() {
        return measurementTime;
    }
}
