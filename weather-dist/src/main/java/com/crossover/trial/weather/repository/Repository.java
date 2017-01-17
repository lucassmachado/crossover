package com.crossover.trial.weather.repository;

import java.util.Collection;

public interface Repository<T> {

	void add(String iata, T a);

	void remove(String iata);

	T get(String iata);

	Collection<T> getAll();

}
