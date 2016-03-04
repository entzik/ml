package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import java.io.PrintWriter;

/**
 * Created by emilkirschner on 03/03/16.
 */
public class ExtendedContract {
    private final String name;
    private final String brand;
    private final String country;
    private final String timeZone;

    public ExtendedContract(String name, String brand, String country, String timeZone) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.timeZone = timeZone;
    }

    public static void writeCSVHeaders(PrintWriter out) {
        out.println("name,brand,country,tz");
    }

    public void toCSV(PrintWriter out) {
        out.println(name + "," + brand + "," + country + "," + timeZone);
    }
}
