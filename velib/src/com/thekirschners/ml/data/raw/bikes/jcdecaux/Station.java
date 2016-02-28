package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Station {
    private final int number;
    private final String contract;
    private final String name;
    private final String address;
    private final Coordinates position;
    private final boolean banking;
    private final boolean bonus;
    private final String status;
    private final int bikeStands;
    private final int availableStands;
    private final int availableBikes;
    private final long timestamp;

    @JsonCreator
    public Station(
            @JsonProperty("number") int number,
            @JsonProperty("contract_name") String contract,
            @JsonProperty("name") String name,
            @JsonProperty("address") String address,
            @JsonProperty("position") Coordinates position,
            @JsonProperty("banking") boolean banking,
            @JsonProperty("bonus") boolean bonus,
            @JsonProperty("status") String status,
            @JsonProperty("bike_stands") int bikeStands,
            @JsonProperty("available_bike_stands") int availableStands,
            @JsonProperty("available_bikes") int availableBikes,
            @JsonProperty("last_update") long timestamp) {
        this.number = number;
        this.contract = contract;
        this.name = name;
        this.address = address;
        this.position = position;
        this.banking = banking;
        this.bonus = bonus;
        this.status = status;
        this.bikeStands = bikeStands;
        this.availableStands = availableStands;
        this.availableBikes = availableBikes;
        this.timestamp = timestamp;
    }

    @JsonProperty("number")
    public int getNumber() {
        return number;
    }

    @JsonProperty("contract_name")
    public String getContract() {
        return contract;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("position")
    public Coordinates getPosition() {
        return position;
    }

    @JsonProperty("banking")
    public boolean isBanking() {
        return banking;
    }

    @JsonProperty("bonus")
    public boolean isBonus() {
        return bonus;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("bike_stands")
    public int getBikeStands() {
        return bikeStands;
    }

    @JsonProperty("available_bike_stands")
    public int getAvailableStands() {
        return availableStands;
    }

    @JsonProperty("available_bikes")
    public int getAvailableBikes() {
        return availableBikes;
    }

    @JsonProperty("last_update")
    public long getTimestamp() {
        return timestamp;
    }
}
