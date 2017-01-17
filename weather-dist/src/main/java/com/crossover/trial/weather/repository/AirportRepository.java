package com.crossover.trial.weather.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.crossover.trial.weather.domain.Airport;

public enum AirportRepository implements Repository<Airport> {

	INSTANCE;

	public static Map<String, Airport> db = new ConcurrentHashMap<>(1000);

	public void add(String iata, Airport a) {
		db.put(iata, a);
	}

	public boolean remove(String iata) {
		return db.remove(iata) != null;
	}

	public Airport get(String iata) {
		return db.get(iata);
	}

	public Collection<Airport> getAll() {
		return db.values();
	}

}
