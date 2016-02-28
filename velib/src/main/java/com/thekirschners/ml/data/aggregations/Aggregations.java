package com.thekirschners.ml.data.aggregations;

import com.thekirschners.ml.data.Services;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Contract;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Station;
import com.thekirschners.ml.data.raw.weather.WeatherData;

import java.io.IOException;
import java.util.*;

/**
 * Created by emilkirschner on 24/02/16.
 */
public class Aggregations {

    public static final String JCD_BIKES_API_KEY = "77571707f075ff31ce2dc32fa6da4e9a407c9a0e";
    public static final String OWM_API_KEY = "b77c1ed4bdf983030de5fdbbd444b8f6";

    public static List<StationInfo> jcdBikesData() {
        ArrayList<StationInfo> ret = new ArrayList<>();

        HashMap<String, WeatherData> weather = new HashMap<>();
        HashMap<String, List<Station>> stations = new HashMap<>();

        List<Contract> contracts = Services.jcdBikeService().contracts(JCD_BIKES_API_KEY);
        for (Contract contract : contracts) {
            List<Station> contractStations = Services.jcdBikeService().stations(JCD_BIKES_API_KEY, contract.getName());
            stations.put(contract.getName(), contractStations);
            for (String city : contract.getCities()) {
                String where = city + "," + contract.getCountry();
                try {
                    WeatherData metric = Services.weather().now(OWM_API_KEY, where, "metric");
                    weather.put(city, metric);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (Station station : contractStations) {
                String city = contract.getCities()[0];
                WeatherData weatherData = weather.get(city);
/*
                StationInfo stationInfo = new StationInfo(
                        calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.DAY_OF_MONTH), new Date().getTime(), contract.getName(),
                        station.getName(),
                        station.getStatus(),
                        station.getBikeStands(),
                        station.getAvailableStands(),
                        station.getAvailableBikes(),
                        weatherData.condition()[0].main(),
                        weatherData.metrics().temperature(),
                        weatherData.wind().speed()
                );
                ret.add(stationInfo);
*/
            }
        }

        return Collections.unmodifiableList(ret);
    }

    public static void main(String[] args) throws IOException {
    }
}
