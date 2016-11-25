package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.inputs.Timer;

public class ProfiledRangeOut implements ScalarOutput {
	double max, min, maxChange;
	Timer timer;
	double setpoint, goalPoint;
	ScalarOutput controller;

	protected ProfiledRangeOut(ScalarOutput controller,double min,double max, double maxChange) {
		this.maxChange = maxChange;
		this.controller = controller;
		this.min=min;
		this.max=max;
		timer = new Timer();
	}

	@Override
	public void set(double goalPoint) {
		setpoint = getNextSetpoint(goalPoint, timer.get());
		controller.set(setpoint);
		timer.zero();
	}

	private double getNextSetpoint(double goalPoint, double timeDiff) {
		double maxChangeInSetpoint = maxChange * timeDiff;
		double newSetpoint;

		if (Math.abs(goalPoint - setpoint) < maxChangeInSetpoint) {
			newSetpoint = goalPoint;
		} else if (goalPoint > setpoint) {
			newSetpoint = setpoint + maxChangeInSetpoint;
		} else {
			newSetpoint = setpoint - maxChangeInSetpoint;
		}

		if (newSetpoint > max) {
			newSetpoint = max;
		} else if (newSetpoint < min) {
			newSetpoint = min;
		}

		return newSetpoint;
	}

}
