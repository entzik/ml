package com.thekirschners.ml.data;

import com.thekirschners.ml.data.aggregations.StationInfo;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Contract;
import com.thekirschners.ml.data.raw.bikes.jcdecaux.Station;
import com.thekirschners.ml.data.raw.weather.WeatherData;
import org.apache.commons.cli.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.Calendar;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class BikesDataSetBuilder {
    public static final String EVERY_5_MINUTES = "0 0/5 * * * ?";

    public static final String VELIB_API_KEY = "77571707f075ff31ce2dc32fa6da4e9a407c9a0e";
    public static final String WEATHER_API_KEY = "b77c1ed4bdf983030de5fdbbd444b8f6";

    public static void main(String[] args) {

        Option dataDirOption   = Option.builder().argName("outputDir")
                .longOpt("outputDir")
                .hasArg()
                .desc(  "use given file for log" )
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .build();

        Options options = new Options();
        options.addOption(dataDirOption);

        try {
            CommandLine parse = new DefaultParser().parse(options, args);
            String outputDir = parse.getOptionValue("outputDir");
            startJob(outputDir);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private static void startJob(String outputDir) {
        try {
            JobDetail job = JobBuilder.newJob(BikeAndWeatherDataJob.class)
                    .usingJobData("outputDir", outputDir)
                    .withIdentity("BikesAndWeather", "BikesAndWeather")
                    .build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("every 5 minutes", "BikesAndWeather")
                    .withSchedule(CronScheduleBuilder.cronSchedule(EVERY_5_MINUTES))
                    .build();

            // schedule it
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static class BikeAndWeatherDataJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            final String outputDir = (String) context.getMergedJobDataMap().get("outputDir");
            long time = new Date().getTime();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            final List<Contract> contracts = Services.jcdBikeService().contracts(VELIB_API_KEY);

            final int month = calendar.get(Calendar.MONTH);
            final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            final int minute = calendar.get(Calendar.MINUTE);
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            final int year = calendar.get(Calendar.YEAR);

            for (Contract contract : contracts) {
                final String where = (contract.getName() + "," + contract.getCountry()).toLowerCase().replace("valence","valencia").replace("bruxelles-capitale", "bruxelles");
                final WeatherData metric = Services.weather().now(WEATHER_API_KEY, where, "metric");
                final List<Station> stations = Services.jcdBikeService().stations(VELIB_API_KEY, contract.getName());
                PrintWriter out = null;
                try {
                    final String fileName = "bikes-" + contract.getName() + "-" + year + "_" + month + "_" + dayOfMonth + ".csv";
                    final File outputFile = new File(new File(outputDir), fileName);
                    out = new PrintWriter(new FileOutputStream(outputFile, true), true);
                    for (Station station : stations) {
                        StationInfo stationInfo = new StationInfo(
                                minute,
                                hour,
                                dayOfWeek,
                                dayOfMonth,
                                month,
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
                } finally {
                    if (out != null)
                        out.close();
                }
            }
        }
    }

}
