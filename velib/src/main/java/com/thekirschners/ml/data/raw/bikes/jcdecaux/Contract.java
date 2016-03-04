package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Contract {
    private static Map<String,String> getTimeZones() {
        HashMap<String,String> map = new HashMap<>(); 
        map.put("Rouen","Europe/Paris");
        map.put("Paris","Europe/Paris");
        map.put("Toulouse","Europe/Paris");
        map.put("Luxembourg","Europe/Luxembourg");
        map.put("Dublin","Europe/Dublin");
        map.put("Valence","Europe/Madrid");
        map.put("Stockholm","Europe/Stockholm");
        map.put("Goteborg","Europe/Stockholm");
        map.put("Santander","Europe/Madrid");
        map.put("Lund","Europe/Stockholm");
        map.put("Amiens","Europe/PAris");
        map.put("Lillestrom","Europe/Oslo");
        map.put("Mulhouse","Europe/Paris");
        map.put("Lyon","Europe/Paris");
        map.put("Ljubljana","Europe/Ljubljana");
        map.put("Seville","Europe/Madrid");
        map.put("Namur","Europe/Brussels");
        map.put("Nancy","Europe/Paris");
        map.put("Creteil","Europe/Paris");
        map.put("Bruxelles-Capitale","Europe/Brussels");
        map.put("Cergy-Pontoise","Europe/Paris");
        map.put("Vilnius","Europe/Vilnius");
        map.put("Toyama","Asia/Tokyo");
        map.put("Kazan","Europe/Moscow");
        map.put("Marseille","Europe/Paris");
        map.put("Nantes","Europe/Paris");
        map.put("Besancon","Europe/Paris");
        return map;
    }

    private static Map<String,String> TIME_ZONES = getTimeZones();
    
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

    public TimeZone getTimeZone() {
        String id = TIME_ZONES.get(name);
        return id == null ?  TimeZone.getDefault() : TimeZone.getTimeZone(id);
    }

    public List<CountryCity> cities() {
        return Arrays.asList(cities).stream().map(c -> new CountryCity(c, country)).collect(Collectors.toList());
    }

    public static void writeCSVHeaders(PrintWriter out) {
        out.println("name,brand,country");
    }

    public static Set<String> supportedContracts() {
        return TIME_ZONES.keySet();
    }

    public void toCSV(PrintWriter out) {
        out.println(name + "," + brand + "," + country);
    }
}
