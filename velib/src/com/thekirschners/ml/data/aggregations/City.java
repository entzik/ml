package com.thekirschners.ml.data.aggregations;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class City {
    final String id;
    final String name;
    final double lon;
    final double lat;
    final String country;

    public City(String line) {
        String[] split = line.split("\t");
        this.id = split[0];
        this.name = split[1];
        this.lon = Double.parseDouble(split[2]);
        this.lat = Double.parseDouble(split[3]);
        this.country = split[4];
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getCountry() {
        return country;
    }

    public String cityCountry() {
        return (name + "," + country).toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        City other = (City) obj;
        return cityCountry().equals(other.cityCountry());
    }
}
