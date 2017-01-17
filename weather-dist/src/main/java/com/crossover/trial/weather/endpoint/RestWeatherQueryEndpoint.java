package com.crossover.trial.weather.endpoint;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportRepository;
import com.crossover.trial.weather.repository.AtmosphereInfoRepository;
import com.crossover.trial.weather.service.RequestFrequencyService;
import com.crossover.trial.weather.service.WeatherService;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	public final static Logger LOGGER = Logger.getLogger("WeatherQuery");

	/** shared gson json to object factory */
	public static final Gson gson = new Gson();

	private WeatherService weatherService;

	public RestWeatherQueryEndpoint() {
		this.weatherService = new WeatherService(new RequestFrequencyService(), AirportRepository.INSTANCE,
				AtmosphereInfoRepository.INSTANCE);
	}

	/**
	 * Retrieve service health including total size of valid data points and
	 * request frequency information.
	 *
	 * @return health stats for the service as a string
	 */
	@GET
  @Path("/ping")
	@Override
	public String ping() {
		return gson.toJson(weatherService.getServiceHealth().getValues());
	}

	/**
	 * Given a query in json format {'iata': CODE, 'radius': km} extracts the
	 * requested airport information and return a list of matching atmosphere
	 * information.
	 *
	 * @param iata the iataCode
	 * @param radius the radius in km
	 *
	 * @return a list of atmospheric information
	 */
	@GET
	@Path("/weather/{iata}/{radius}")
	@Override
	public Response weather(@PathParam("iata") String iata, @PathParam("radius") String radiusString) {
		List<AtmosphericInformation> informations = weatherService.getAtmosphericInformations(iata, radiusString);
		if (informations == null || informations.isEmpty()) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}

		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON_TYPE).entity(informations).build();
	}

}
