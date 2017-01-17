package com.crossover.trial.weather.domain;

import java.util.HashMap;
import java.util.Map;

public class ServiceHealth {

	private int dataPointSize;

	private Map<String, Double> iataFrequency;

	private int[] radiusFrequency;

	public void setDataPointSize(int dataPointSize) {
		this.dataPointSize = dataPointSize;
	}

	public void setIataFrequency(Map<String, Double> iataFrequency) {
		this.iataFrequency = iataFrequency;
	}

	public void setRadiusFrequency(int[] radiusFrequency) {
		this.radiusFrequency = radiusFrequency;
	}

	public Map<String, Object> getValues() {
		Map<String, Object> values = new HashMap<>();
		values.put("datasize", dataPointSize);
		values.put("iata_freq", iataFrequency);
		values.put("radius_freq", radiusFrequency);
		return values;
	}

}
