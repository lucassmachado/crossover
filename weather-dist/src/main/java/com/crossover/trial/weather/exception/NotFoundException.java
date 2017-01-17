package com.crossover.trial.weather.exception;

public class NotFoundException extends WeatherException {

	private static final long serialVersionUID = 3065449028206293612L;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
