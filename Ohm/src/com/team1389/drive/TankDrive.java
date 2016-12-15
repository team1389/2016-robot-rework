package com.team1389.drive;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.util.DriveSignal;

public class TankDrive {
	private DriveOut<Percent> output;
	private PercentIn throttle;
	private PercentIn wheel;

	public TankDrive(DriveOut<Percent> output, PercentIn throttle, PercentIn wheel) {
		this.output = output;
		this.throttle = throttle;
		this.wheel = wheel;
	}

	public void update() {
		double x = wheel.get();
		double y = throttle.get();
		output.set(new DriveSignal(-y + x, -y - x));
	}

}
