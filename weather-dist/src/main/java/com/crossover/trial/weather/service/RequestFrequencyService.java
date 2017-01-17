package com.crossover.trial.weather.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.crossover.trial.weather.domain.Airport;
import com.crossover.trial.weather.domain.AtmosphericInformation;

public class RequestFrequencyService {

	public static Map<Airport, Integer> request = new ConcurrentHashMap<>();

	public static Map<Double, Integer> radius = new ConcurrentHashMap<>();

	public int getValidDataPointSize(Set<AtmosphericInformation> atmosphericInformations) {
		int datasize = 0;
		if (atmosphericInformations != null) {
			for (AtmosphericInformation ai : atmosphericInformations) {
				if (ai.hasRecentReadings() && ai.updatedInTheLastDay()) {
					datasize++;
				}
			}
		}
		return datasize;
	}

	public int[] getRadiusFrequency() {
		int m = radius.keySet().stream().max(Double::compare).orElse(1000.0).intValue() + 1;

		int[] hist = new int[m];
		Set<Entry<Double, Integer>> radiusEntries = radius.entrySet();
		for (Map.Entry<Double, Integer> entrie : radiusEntries) {
			int i = entrie.getKey().intValue() % 10;
			hist[i] += entrie.getValue();
		}
		return hist;
	}

	public Map<String, Double> getIataFrequency(Set<Airport> airports) {
		Map<String, Double> iataFrequency = new HashMap<>();

		int sizeRequests = request.size();
		for (Airport airport : airports) {
			Integer frequencyAirport = request.getOrDefault(airport, 0);
			double fraction = (double) frequencyAirport / sizeRequests;

			iataFrequency.put(airport.getIata(), fraction);
		}

		return iataFrequency;
	}

	/**
	 * Records information about how often requests are made
	 *
	 * @param airport
	 * @param radiusDouble
	 *            query radius
	 */
	public void updateRequestFrequency(Airport airport, Double radiusDouble) {
		if (airport != null) {
			request.put(airport, request.getOrDefault(airport, 0) + 1);
		}

		if (radiusDouble != null) {
			radius.put(radiusDouble, radius.getOrDefault(radiusDouble, 0));
		}
	}

}
