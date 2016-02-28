package com.thekirschners.ml.data.raw.weather;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class WeatherCondition {
    final int id;
    final String main;
    final String description;
    final String icon;

    @JsonCreator
    public WeatherCondition(
            @JsonProperty("id") int id,
            @JsonProperty("main") String main,
            @JsonProperty("description") String description,
            @JsonProperty("icon") String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    @JsonProperty("id")
    public int id() {
        return id;
    }

    @JsonProperty("main")
    public String main() {
        return main;
    }

    @JsonProperty("description")
    public String description() {
        return description;
    }

    @JsonProperty("icon")
    public String icon() {
        return icon;
    }
}
