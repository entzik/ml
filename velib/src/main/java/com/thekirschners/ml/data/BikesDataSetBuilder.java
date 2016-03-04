package com.thekirschners.ml.data;

import com.thekirschners.ml.data.raw.bikes.jcdecaux.DynamicStationData;
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
//            startJob(outputDir);
            new BikeAndWeatherDataJob().saveData(outputDir);
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
            saveData(outputDir);
        }

        public void saveData(String outputDir) {
            final List<Contract> contracts = Services.jcdBikeService().contracts(VELIB_API_KEY);

            saveContractOfDay(outputDir, contracts);

            for (Contract contract : contracts) {
                if (!Contract.supportedContracts().contains(contract.getName()))
                    continue;

                final String city = contract.getName().toLowerCase().replace("valence", "valencia").replace("bruxelles-capitale", "bruxelles");
                final TimeZone tz = contract.getTimeZone();

                final Calendar calendar = Calendar.getInstance(tz);
                calendar.setTimeInMillis(new Date().getTime());
                final int month = calendar.get(Calendar.MONTH);
                final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final int minute = calendar.get(Calendar.MINUTE);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                final int year = calendar.get(Calendar.YEAR);

                final String where = (city + "," + contract.getCountry().toLowerCase());
                final WeatherData weatherData = Services.weather().now(WEATHER_API_KEY, where, "metric");
                final List<Station> stations = Services.jcdBikeService().stations(VELIB_API_KEY, contract.getName());
                saveStationStaticData(city,outputDir, stations, year, month, dayOfMonth);
                saveWeatherForContract(city,outputDir, weatherData, year, month, dayOfMonth, hour, minute);
                saveStationsDynamicData(city,outputDir, month, dayOfMonth, minute, hour, dayOfWeek, year, contract, weatherData, stations);
            }
        }

        private TimeZone timezoneForCity(String city) {
            String[] availableIDs = TimeZone.getAvailableIDs();
            for (String id : availableIDs)
                if (id.toLowerCase().contains(city)) {
                    System.out.println("id = " + id);
                    return TimeZone.getTimeZone(id);
                }
            return TimeZone.getDefault();
        }

        private void saveWeatherForContract(String city, String outputDir, WeatherData weatherData, int year, int month, int dayOfMonth, int hour, int minute) {
            final String fileName = "weather-" + city + "-" + year + "_" + month + ".csv";
            final File outputFile = new File(new File(outputDir), fileName);
            boolean writeHeaders = !outputFile.exists();
            PrintWriter out = null;
            try {
                out = new PrintWriter(new FileOutputStream(outputFile, true), true);
                if (writeHeaders)
                    WeatherData.writeCSVHeaders(out);
                weatherData.toCSV(year, month, dayOfMonth, hour, minute, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (out != null)
                    out.close();
            }
        }

        private void saveContractOfDay(final String outputDir, final List<Contract> contracts) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            final int month = calendar.get(Calendar.MONTH);
            final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            final int year = calendar.get(Calendar.YEAR);

            final String fileName = "bike-contracts-" + year + "_" + month + "_" + dayOfMonth + ".csv";
            final File outputFile = new File(new File(outputDir), fileName);
            if (!outputFile.exists()) {
                PrintWriter out = null;
                try {
                    out = new PrintWriter(new FileOutputStream(outputFile, true), true);
                    Contract.writeCSVHeaders(out);
                    for (Contract contract : contracts)
                        contract.toCSV(out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null)
                        out.close();
                }
            }
        }

        private void saveStationStaticData(String city, String outputDir, List<Station> stations, int year, int month, int dayOfMonth) {
            final String fileName = "stations-" + city + "-" + year + "_" + month + "_" + dayOfMonth + ".csv";
            final File outputFile = new File(new File(outputDir), fileName);
            if (!outputFile.exists()) {
                PrintWriter out = null;
                try {
                    out = new PrintWriter(new FileOutputStream(outputFile, true), true);
                    Station.writeCSVHeaders(out);
                    for (Station station : stations)
                        station.toCSV(out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null)
                        out.close();
                }
            }
        }

        private void saveStationsDynamicData(String city, String outputDir, int month, int dayOfMonth, int minute, int hour, int dayOfWeek, int year, Contract contract, WeatherData metric, List<Station> stations) {
            HashMap<String,PrintWriter> writers = null;
            try {
                writers = new HashMap<>();
                for (Station station : stations) {
                    final String fileName = "bikes-" + city + "-" + station.getNumber() + "-" + year + "_" + month + ".csv";
                    final File outputFile = new File(new File(outputDir), fileName);
                    boolean writeHeaders = !outputFile.exists();
                    PrintWriter out = writers.get(fileName);
                    if (out == null) {
                        out = new PrintWriter(new FileOutputStream(outputFile, true), true);
                        writers.put(fileName, out);
                    }
                    if (writeHeaders)
                        DynamicStationData.writeCSVHeaders(out);

                    DynamicStationData dynamicStationData = new DynamicStationData(
                            minute,
                            hour,
                            dayOfWeek,
                            dayOfMonth,
                            month,
                            station.getNumber(),
                            station.getStatus(),
                            station.getBikeStands(),
                            station.getAvailableStands(),
                            station.getAvailableBikes()
                    );
                    dynamicStationData.toCSV(out);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (writers != null)
                    writers.values().forEach(PrintWriter::close);
            }
        }
    }

}
