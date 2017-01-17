package com.crossover.trial.weather.exception;

/**
 * An internal exception marker
 */
public class WeatherException extends RuntimeException {

	private static final long serialVersionUID = -3717617059428945841L;

	public WeatherException(String message) {
		super(message);
	}

	public WeatherException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
