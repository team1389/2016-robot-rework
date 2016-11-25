package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.inputs.Timer;
import com.team1389.hardware.interfaces.outputs.RangeOut;

public class ProfiledRangeOut implements RangeOut {
	double maxPos, minPos, maxChange;
	Timer timer;
	double setpoint, goalPoint;
	RangeOut controller;

	protected ProfiledRangeOut(RangeOut controller, double maxChange) {
		this.maxPos = controller.max();
		this.minPos = controller.min();
		this.maxChange = maxChange;
		this.controller = controller;

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

		if (newSetpoint > maxPos) {
			newSetpoint = maxPos;
		} else if (newSetpoint < minPos) {
			newSetpoint = minPos;
		}

		return newSetpoint;
	}

	@Override
	public double min() {
		return minPos;
	}

	@Override
	public double max() {
		return maxPos;
	}

}
