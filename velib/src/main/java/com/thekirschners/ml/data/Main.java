package com.thekirschners.ml.data;

import com.thekirschners.ml.data.aggregations.StationInfo;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Contract;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Station;
import com.thekirschners.ml.data.raw.weather.WeatherData;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.Calendar;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class Main {
    public static final String EVERY_5_MINUTES = "* * * * * ?";

    public static final String VELIB_API_KEY = "77571707f075ff31ce2dc32fa6da4e9a407c9a0e";
    public static final String WEATHER_API_KEY = "b77c1ed4bdf983030de5fdbbd444b8f6";

    public static void main(String[] args){
        try {
            JobDetail job = JobBuilder.newJob(XJob.class)
                    .withIdentity("dummyJobName", "group1").build();


            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("dummyTriggerName", "group1")
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(300).repeatForever())
                    .build();

            // schedule it
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static class XJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("XJob.execute");
            long time = new Date().getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            List<Contract> contracts = Services.jcdBikeService().contracts(VELIB_API_KEY);
            for (Contract contract : contracts) {
                String where = (contract.getName() + "," + contract.getCountry()).toLowerCase().replace("valence","valencia").replace("bruxelles-capitale", "bruxelles");
                WeatherData metric = Services.weather().now(WEATHER_API_KEY, where, "metric");
                List<Station> stations = Services.jcdBikeService().stations(VELIB_API_KEY, contract.getName());
                try {
                    PrintWriter out = new PrintWriter(new FileOutputStream("bikes-" + contract.getName(), true), true);
                    for (Station station : stations) {
                        StationInfo stationInfo = new StationInfo(
                                calendar.get(Calendar.MINUTE),
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH),
                                contract.getName(),
                                station.getName(),
                                station.getStatus(),
                                station.getBikeStands(),
                                station.getAvailableStands(),
                                station.getAvailableBikes(),
                                metric.condition()[0].main(),
                                metric.metrics().temperature(),
                                metric.wind().speed()
                        );
                        stationInfo.toCSV(out);
                    }
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class MyJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
        }
    }
}
