This module contains code to build a city bike sharing services data set for services operated by JCDecaux. Data is extracted using 
the JCDecaux open data API at https://developer.jcdecaux.com/#/opendata/vls?page=getstarted and enriched with weather information 
for cities where services operate. Weather data is obtained from OpenWeatherMap - http://openweathermap.org/api.

The goal is to train machine learning models using this dataset and be able to predict bike availability in a specified station based 
on a particular future date and time and a weather forecast.

The entry point is the BikesDataSetBuilder class. It takes three command line parameters: one to specify the directory where the 
data should be downloaded, one to specify the JCDecaux API key and one to specify the open weather map API key.

The data set builder will pull station status and current weather data every 5 minutes and dump it to csv files. It will be one 
CVS file per station per month anx one weather file per station per month.
