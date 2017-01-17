package com.crossover.trial.weather.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class Airport {

	/** earth radius in KM */
	public static final double R = 6372.8;

	/** the three letter IATA code */
	private String iata;

	/** latitude value in degrees */
	private double latitude;

	/** longitude value in degrees */
	private double longitude;

	public Airport() {
		super();
	}

	public Airport(String iata, double latitude, double longitude) {
		this.iata = iata;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Haversine distance between two airports.
	 *
	 * @param other
	 *            airport
	 * @return the distance in KM
	 */
	public double calculateDistance(Airport other) {
		double deltaLat = Math.toRadians(other.latitude - latitude);
		double deltaLon = Math.toRadians(other.longitude - longitude);
		double a = Math.pow(Math.sin(deltaLat / 2), 2)
				+ Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(latitude) * Math.cos(other.latitude);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iata == null) ? 0 : iata.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Airport) {
			return ((Airport) other).iata.equals(this.iata);
		}

		return false;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
