package com.crossover.trial.weather.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.crossover.trial.weather.domain.AtmosphericInformation;

public enum AtmosphereInfoRepository implements Repository<AtmosphericInformation> {

	INSTANCE;

	public static Map<String, AtmosphericInformation> db = new ConcurrentHashMap<>();

	public void add(String iata, AtmosphericInformation a) {
		db.put(iata, a);
	}

	public boolean remove(String iata) {
		return db.remove(iata) != null;
	}

	public AtmosphericInformation get(String iata) {
		return db.get(iata);
	}

	public Collection<AtmosphericInformation> getAll() {
		return db.values();
	}

}
