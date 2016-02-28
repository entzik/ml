package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Contract {
    private final String name;
    private final String brand;
    private final String country;
    private final String[] cities;

    @JsonCreator
    public Contract(
            @JsonProperty("name") String name,
            @JsonProperty("commercial_name") String brand,
            @JsonProperty("country_code") String country,
            @JsonProperty("cities") String[] cities) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.cities = cities;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("commercial_name")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("country_code")
    public String getCountry() {
        return country;
    }

    @JsonProperty("cities")
    public String[] getCities() {
        return cities;
    }

    public List<CountryCity> cities() {
        return Arrays.asList(cities).stream().map(c -> new CountryCity(c, country)).collect(Collectors.toList());
    }
}
