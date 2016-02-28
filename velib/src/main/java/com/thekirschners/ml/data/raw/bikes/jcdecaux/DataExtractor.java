package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import java.util.List;

public class DataExtractor {
    public void fetchAndStoreStatus(List<Contract> contracts)  {
        JcdBikeService bikeService = BikeServiceFactory.instance().get();
        for (Contract contract : contracts) {
            List<Station> stations = bikeService.stations(BikeServiceFactory.API_KEY, contract.getName());
            for (Station station : stations) {

            }
        }
    }
}
