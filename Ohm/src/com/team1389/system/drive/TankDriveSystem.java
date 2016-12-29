package com.team1389.system.drive;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.AddList;
import com.team1389.util.DriveSignal;
import com.team1389.watch.Watchable;

public class TankDriveSystem extends Subsystem {
	private DriveOut<Percent> output;
	private PercentIn throttle;
	private PercentIn wheel;

	public TankDriveSystem(DriveOut<Percent> output, PercentIn throttle, PercentIn wheel) {
		this.output = output;
		this.throttle = throttle;
		this.wheel = wheel;
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		double x = wheel.get();
		double y = throttle.get();
		output.set(new DriveSignal(-y + x, -y - x));
	}

	@Override
	public String getName() {
		return "Tank Drive";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return null;
	}

}
