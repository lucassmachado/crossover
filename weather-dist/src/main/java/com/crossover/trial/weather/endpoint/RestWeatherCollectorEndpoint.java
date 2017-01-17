package com.crossover.trial.weather.endpoint;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherException;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.domain.DataPointType;
import com.crossover.trial.weather.repository.AirportRepository;
import com.crossover.trial.weather.repository.AtmosphereInfoRepository;
import com.crossover.trial.weather.service.RequestFrequencyService;
import com.crossover.trial.weather.service.WeatherService;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {

    public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

    /** shared gson json to object factory */
    public final static Gson gson = new Gson();
    
    private WeatherService weatherService;
    
    public RestWeatherCollectorEndpoint() {
		this.weatherService = new WeatherService(
				new RequestFrequencyService(),
				AirportRepository.INSTANCE,
				AtmosphereInfoRepository.INSTANCE);
	}

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(@PathParam("iata") String iataCode,
                                  @PathParam("pointType") String pointType,
                                  String datapointJson) {
        try {
            addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response getAirports() {
        return Response.status(Response.Status.OK).entity(weatherService.getAirports()).build();
    }

    @Override
    public Response getAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.OK).entity(weatherService.getAirport(iata)).build();
    }

    @Override
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {

    	if (iata == null || iata.length() != 3 || latString == null || longString == null) {
            LOGGER.log(Level.SEVERE, "Bad parameters: iata = " + iata + ", latString = " + latString + ", longString = " + longString);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    	weatherService.addAirport(iata, latString, longString);
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response deleteAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
    //
    // Internal support methods
    //

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    private void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
    	weatherService.addDataPoint(iataCode, pointType, dp);
    }

}
