package com.crossover.trial.weather.service.template.impl;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;
import com.crossover.trial.weather.service.template.AtmosphericInformationUpdater;

public class CloudCoverUpdater extends AtmosphericInformationUpdater {

	public CloudCoverUpdater(AtmosphericInformation atmosphericInformation, DataPoint dataPoint) {
		super(atmosphericInformation, dataPoint);
	}

	@Override
	protected boolean isMeanMatch() {
		return dataPoint.getMean() >= 0 && dataPoint.getMean() < 100;
	}

	@Override
	protected void setDataPoint() {
		atmosphericInformation.setCloudCover(dataPoint);
	}

}
