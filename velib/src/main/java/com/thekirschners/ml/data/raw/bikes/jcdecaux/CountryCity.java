package com.thekirschners.ml.data.raw.bikes.jcdecaux;

/**
* Created by emilkirschner on 24/02/16.
*/
public class CountryCity {
    final String city;
    final String country;

    public CountryCity(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
