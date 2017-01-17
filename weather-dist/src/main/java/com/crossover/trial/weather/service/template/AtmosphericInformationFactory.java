package com.crossover.trial.weather.service.template;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.domain.DataPointType;
import com.crossover.trial.weather.service.template.impl.CloudCoverUpdater;
import com.crossover.trial.weather.service.template.impl.HumidtyUpdater;
import com.crossover.trial.weather.service.template.impl.PrecipitationUpdater;
import com.crossover.trial.weather.service.template.impl.PressureUpdater;
import com.crossover.trial.weather.service.template.impl.TemperatureUpdater;
import com.crossover.trial.weather.service.template.impl.WindUpdater;

public class AtmosphericInformationFactory {

	public static AtmosphericInformationUpdater getUpdaterTemplate(AtmosphericInformation atmosphericInformation,
			DataPoint dataPoint, String pointType) {
		if (pointType == null || pointType.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid pointType!");
		}

		DataPointType type = DataPointType.valueOf(pointType.toUpperCase());
		switch (type) {
		case WIND:
			return new WindUpdater(atmosphericInformation, dataPoint);
		case TEMPERATURE:
			return new TemperatureUpdater(atmosphericInformation, dataPoint);
		case HUMIDTY:
			return new HumidtyUpdater(atmosphericInformation, dataPoint);
		case PRESSURE:
			return new PressureUpdater(atmosphericInformation, dataPoint);
		case CLOUDCOVER:
			return new CloudCoverUpdater(atmosphericInformation, dataPoint);
		case PRECIPITATION:
			return new PrecipitationUpdater(atmosphericInformation, dataPoint);
		}
		return null;
	}

}
