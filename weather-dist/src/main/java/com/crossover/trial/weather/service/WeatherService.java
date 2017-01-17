package com.crossover.trial.weather.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.crossover.trial.weather.domain.Airport;
import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.domain.ServiceHealth;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.service.template.AtmosphericInformationFactory;
import com.crossover.trial.weather.service.template.AtmosphericInformationUpdater;

public class WeatherService {

	private RequestFrequencyService requestFrequencyService;

	private Repository<Airport> airportRepository;

	private Repository<AtmosphericInformation> atmosphericInfoRepository;

	public WeatherService(RequestFrequencyService requestFrequencyService, Repository<Airport> airportRepository,
			Repository<AtmosphericInformation> atmosphericInfoRepository) {
		this.requestFrequencyService = requestFrequencyService;
		this.airportRepository = airportRepository;
		this.atmosphericInfoRepository = atmosphericInfoRepository;
	}

	public Set<Airport> getAirports() {
		return new LinkedHashSet<>(airportRepository.getAll());
	}

	/**
	 * Given an iata find the airport data
	 *
	 * @param iata
	 *            as a string
	 * @return airport data or null if not found
	 */
	public Airport getAirport(String iata) {
		return airportRepository.get(iata);
	}

	public void addAirport(String iata, String latitude, String longitude) {
		Airport a = new Airport();
		a.setIata(iata);
		a.setLatitude(Double.valueOf(latitude));
		a.setLongitude(Double.valueOf(longitude));

		airportRepository.add(iata, a);

		atmosphericInfoRepository.add(iata, new AtmosphericInformation());
	}

	public void deleteAirport(String iata) {
		airportRepository.remove(iata);
	}

	public void addDataPoint(String iataCode, String pointType, DataPoint dataPoint) {
		AtmosphericInformation atmosphericInformation = atmosphericInfoRepository.get(iataCode);
		if (atmosphericInformation == null) {
			throw new NoSuchElementException();
		}

		updateAtmosphericInformation(atmosphericInformation, pointType, dataPoint);
	}

	/**
	 * update atmospheric information with the given data point for the given
	 * point type
	 *
	 * @param ai
	 *            the atmospheric information object to update
	 * @param pointType
	 *            the data point type as a string
	 * @param dataPoint
	 *            the actual data point
	 */
	private void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dataPoint) {
		AtmosphericInformationUpdater updater = AtmosphericInformationFactory.getUpdaterTemplate(ai, dataPoint,
				pointType);
		if (updater == null) {
			throw new IllegalStateException("couldn't update atmospheric data");
		} else {
			updater.update();
		}
	}

	public Set<AtmosphericInformation> getAtmosphericInformations() {
		return new LinkedHashSet<>(atmosphericInfoRepository.getAll());
	}

	public List<AtmosphericInformation> getAtmosphericInformations(String iata, String radiusString) {
		double radius = getRadius(radiusString);
		updateRequestFrequency(iata, radius);

		List<AtmosphericInformation> atmosphericInformations = new ArrayList<>();
		if (radius == 0) {
			atmosphericInformations.add(atmosphericInfoRepository.get(iata));
		} else {
			Airport foundAirport = getAirport(iata);
			for (Airport airport : getAirports()) {
				if (foundAirport != null && foundAirport.calculateDistance(airport) <= radius) {
					AtmosphericInformation foundAi = atmosphericInfoRepository.get(iata);
					if (foundAi != null && foundAi.hasRecentReadings()) {
						atmosphericInformations.add(foundAi);
					}
				}
			}
		}
		return atmosphericInformations;
	}

	private double getRadius(String radiusString) {
		double radius = 0;
		try {
			radius = Double.valueOf(radiusString);
		} catch (Exception e) {
		}
		return radius;
	}

	private void updateRequestFrequency(String iata, double radius) {
		Airport airport = getAirport(iata);

		requestFrequencyService.updateRequestFrequency(airport, radius);
	}

	public ServiceHealth getServiceHealth() {
		ServiceHealth health = new ServiceHealth();
		health.setDataPointSize(
				requestFrequencyService.getValidDataPointSize(new LinkedHashSet<>(atmosphericInfoRepository.getAll())));
		health.setIataFrequency(requestFrequencyService.getIataFrequency(getAirports()));
		health.setRadiusFrequency(requestFrequencyService.getRadiusFrequency());
		return health;
	}

}
