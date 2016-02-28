package com.thekirschners.ml.data.raw.bikes.jcdecaux;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

public interface JcdBikeService {
    @GET
    @Path("contracts")
    public List<Contract> contracts(@QueryParam("apiKey") String apiKey);

    @GET
    @Path("stations")
    public List<Station> stations(@QueryParam("apiKey") String apiKey, @QueryParam("contract") String contract);
}
