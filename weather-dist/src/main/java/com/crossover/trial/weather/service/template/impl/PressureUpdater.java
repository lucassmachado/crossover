package com.crossover.trial.weather.service.template.impl;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.service.template.AtmosphericInformationUpdater;

public class PressureUpdater extends AtmosphericInformationUpdater {

	public PressureUpdater(AtmosphericInformation atmosphericInformation, DataPoint dataPoint) {
		super(atmosphericInformation, dataPoint);
	}

	@Override
	protected boolean isMeanMatch() {
		return dataPoint.getMean() >= 650 && dataPoint.getMean() < 800;
	}

	@Override
	protected void setDataPoint() {
		atmosphericInformation.setPressure(dataPoint);
	}

}
