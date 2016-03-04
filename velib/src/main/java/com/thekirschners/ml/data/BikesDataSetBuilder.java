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

    public static final String JOB_PARAM_OUTPUT_DIR = "outputDir";
    public static final String JOB_PARAM_JCD_BIKES_API_KEY = "jcdBikesApiKey";
    public static final String JOB_PARAM_WEATHER_API_KEY = "weatherApiKey";


    public static void main(String[] args) {
        Option dataDirOption   = Option.builder().argName(JOB_PARAM_OUTPUT_DIR)
                .longOpt(JOB_PARAM_OUTPUT_DIR)
                .hasArg()
                .desc(  "dataset output directory" )
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .build();

        Option jcdBikesApiKeyOption   = Option.builder().argName(JOB_PARAM_JCD_BIKES_API_KEY)
                .longOpt(JOB_PARAM_JCD_BIKES_API_KEY)
                .hasArg()
                .desc(  "a valid jcdecaux API key obtained from https://developer.jcdecaux.com/#/signup" )
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .build();

        Option weatherApiKeyOption   = Option.builder().argName(JOB_PARAM_WEATHER_API_KEY)
                .longOpt(JOB_PARAM_WEATHER_API_KEY)
                .hasArg()
                .desc(  "a valid open weather map key obtained from http://openweathermap.org/appid" )
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .build();

        Options options = new Options();
        options.addOption(dataDirOption);
        options.addOption(jcdBikesApiKeyOption);
        options.addOption(weatherApiKeyOption);

        try {
            CommandLine parse = new DefaultParser().parse(options, args);
            String outputDir = parse.getOptionValue(JOB_PARAM_OUTPUT_DIR);
            String jcdBikesApiKey = parse.getOptionValue(JOB_PARAM_JCD_BIKES_API_KEY);
            String weatherApiKey = parse.getOptionValue(JOB_PARAM_WEATHER_API_KEY);
            startJob(outputDir, jcdBikesApiKey, weatherApiKey);
//            new BikeAndWeatherDataJob().saveData(outputDir, jcdBikesApiKey, weatherApiKey);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private static void startJob(String outputDir, String jcdBikesApiKey, String weatherApiKey) {
        try {
            JobDetail job = JobBuilder.newJob(BikeAndWeatherDataJob.class)
                    .usingJobData(JOB_PARAM_OUTPUT_DIR, outputDir)
                    .usingJobData(JOB_PARAM_JCD_BIKES_API_KEY, jcdBikesApiKey)
                    .usingJobData(JOB_PARAM_WEATHER_API_KEY, weatherApiKey)
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
            final String outputDir = (String) context.getMergedJobDataMap().get(JOB_PARAM_OUTPUT_DIR);
            final String jcdBikesApiKey = (String) context.getMergedJobDataMap().get(JOB_PARAM_JCD_BIKES_API_KEY);
            final String weatherApiKey = (String) context.getMergedJobDataMap().get(JOB_PARAM_WEATHER_API_KEY);
            saveData(outputDir, jcdBikesApiKey, weatherApiKey);
        }

        public void saveData(String outputDir, String jcdBikesApiKey, String weatherApiKey) {
            final List<Contract> contracts = Services.jcdBikeService().contracts(jcdBikesApiKey);

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
                final WeatherData weatherData = Services.weather().now(weatherApiKey, where, "metric");
                final List<Station> stations = Services.jcdBikeService().stations(jcdBikesApiKey, contract.getName());
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
