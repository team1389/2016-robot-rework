package com.team1389.hardware.outputs.software;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Value;

public class ProfiledRangeOut<T extends Value> implements ScalarOutput<T> {
	double max, min, maxChange;
	Timer timer;
	double setpoint, goalPoint;
	ScalarOutput<T> controller;

	protected ProfiledRangeOut(ScalarOutput<T> controller, double min, double max, double maxChange,
			double initialPos) {
		this.maxChange = maxChange;
		this.controller = controller;
		this.min = min;
		this.max = max;
		this.setpoint = initialPos;
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
