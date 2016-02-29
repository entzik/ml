package com.thekirschners.ml.data.aggregations;

import java.io.PrintWriter;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class DynamicStationData {
    private final int minute;
    private final int hour;
    private final int dayOfWeek;
    private final int dayOfMonth;
    private final int month;
    private final int number;
    private final String status;
    private final int operationalStands;
    private final int availableStands;
    private final int availableBikes;

    public DynamicStationData(int minute, int hour, int dayOfWeek, int dayOfMonth, int month, int number, String status, int operationalStands, int availableStands, int availableBikes) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.number = number;
        this.status = status;
        this.operationalStands = operationalStands;
        this.availableStands = availableStands;
        this.availableBikes = availableBikes;
    }

    public String status() {
        return status;
    }

    public int bikeStands() {
        return operationalStands;
    }

    public int availableStands() {
        return availableStands;
    }

    public int availableBikes() {
        return availableBikes;
    }


    public void toCSV(PrintWriter out) {
        out.println(month + "," + dayOfMonth + "," + dayOfWeek + "," + hour + "," + minute + "," + number + "," + status + "," + operationalStands + "," + availableStands + "," + availableBikes);
    }
}
