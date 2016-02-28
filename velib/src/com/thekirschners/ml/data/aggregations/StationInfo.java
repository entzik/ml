package com.thekirschners.ml.data.aggregations;

import java.io.PrintWriter;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class StationInfo {
    private final int minute;
    private final int hour;
    private final int dayOfWeek;
    private final int dayOfMonth;
    private final long month;
    private final String contract;
    private final String name;
    private final String status;
    private final int bikeStands;
    private final int availableStands;
    private final int availableBikes;
    private final String weather;
    private final int temperature;
    private final double windSpeed;

    public StationInfo(int minute, int hour, int dayOfWeek, int dayOfMonth, int month, String contract, String name, String status, int bikeStands, int availableStands, int availableBikes, String weather, int temperature, double windSpeed) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.contract = contract;
        this.name = name;
        this.status = status;
        this.bikeStands = bikeStands;
        this.availableStands = availableStands;
        this.availableBikes = availableBikes;
        this.weather = weather;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public String contract() {
        return contract;
    }

    public String name() {
        return name;
    }

    public String status() {
        return status;
    }

    public int bikeStands() {
        return bikeStands;
    }

    public int availableStands() {
        return availableStands;
    }

    public int availableBikes() {
        return availableBikes;
    }

    public String weather() {
        return weather;
    }

    public int temperature() {
        return temperature;
    }

    public double windSpeed() {
        return windSpeed;
    }

    public void toCSV(PrintWriter out) {
        out.println(minute + "," + hour + "," + dayOfWeek + "," + dayOfMonth + "," + dayOfMonth + "," + contract + "," + name + "," + status + "," + bikeStands + "," + bikeStands + "," + availableStands + "," + availableBikes + "," + weather + "," + temperature + "," + windSpeed);
    }
}
