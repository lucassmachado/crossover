package com.crossover.trial.weather.endpoint;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.domain.Airport;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.exception.NotFoundException;
import com.crossover.trial.weather.repository.AirportRepository;
import com.crossover.trial.weather.repository.AtmosphereInfoRepository;
import com.crossover.trial.weather.service.RequestFrequencyService;
import com.crossover.trial.weather.service.WeatherService;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
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
		this.weatherService = new WeatherService(new RequestFrequencyService(), AirportRepository.INSTANCE,
				AtmosphereInfoRepository.INSTANCE);
	}

	@GET
	@Path("/ping")
	@Override
	public Response ping() {
		return Response.status(Response.Status.OK).entity("ready").build();
	}

	@POST
	@Path("/weather/{iata}/{pointType}")
	@Override
	public Response updateWeather(@PathParam("iata") String iataCode, @PathParam("pointType") String pointType,
			String datapointJson) {
		try {
			weatherService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/airports")
	@Override
	public Response getAirports() {
		Set<Airport> airports = weatherService.getAirports();
		if (airports == null || airports.isEmpty()) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}

		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON_TYPE).entity(airports).build();
	}

	@GET
	@Path("/airport/{iata}")
	@Override
	public Response getAirport(@PathParam("iata") String iata) {
		Airport airport = weatherService.getAirport(iata);
		if (airport == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Airport with iata " + iata + " not found.").build();
		}
		
		return Response.status(Response.Status.OK).entity(airport).build();
	}

	@POST
	@Path("/airport/{iata}/{lat}/{long}")
	@Override
	public Response addAirport(@PathParam("iata") String iata, @PathParam("lat") String latString,
			@PathParam("long") String longString) {

		if (iata == null || iata.length() != 3 || latString == null || longString == null) {
			LOGGER.log(Level.SEVERE,
					"Bad parameters: iata = " + iata + ", latString = " + latString + ", longString = " + longString);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		weatherService.addAirport(iata, latString, longString);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("/airport/{iata}")
	@Override
	public Response deleteAirport(@PathParam("iata") String iata) {
		boolean deleted = weatherService.deleteAirport(iata);
		if (!deleted) {
			return Response.status(Response.Status.NOT_FOUND).entity(iata + " not found.").build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/exit")
	@Override
	public Response exit() {
		System.exit(0);
		return Response.noContent().build();
	}

}
