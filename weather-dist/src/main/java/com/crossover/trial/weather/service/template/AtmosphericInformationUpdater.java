package com.crossover.trial.weather.service.template;

import com.crossover.trial.weather.domain.AtmosphericInformation;
import com.crossover.trial.weather.domain.DataPoint;

public abstract class AtmosphericInformationUpdater {

	protected final AtmosphericInformation atmosphericInformation;
	protected final DataPoint dataPoint;

	protected AtmosphericInformationUpdater(AtmosphericInformation atmosphericInformation, DataPoint dataPoint) {
		this.atmosphericInformation = atmosphericInformation;
		this.dataPoint = dataPoint;
	}

	public void update() {
		if (isMeanMatch()) {
			setDataPoint();
			atmosphericInformation.setLastUpdateTime(System.currentTimeMillis());
		}
	}

	protected abstract boolean isMeanMatch();

	protected abstract void setDataPoint();

}
