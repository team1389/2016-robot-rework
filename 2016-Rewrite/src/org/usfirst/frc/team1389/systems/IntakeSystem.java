package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.System;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

public class IntakeSystem extends System {
	PercentIn joystick;
	PercentOut motor;
	DigitalIn IRSensors;
	DigitalIn override;

	boolean isOverride;
	double joyVal;

	public IntakeSystem(PercentOut motor, DigitalIn IRSensors, PercentIn joystick, DigitalIn override) {
		this.motor = motor;
		this.joystick = joystick;
		this.IRSensors = IRSensors;
		this.override = override;
	}

	@Override
	public String getName() {
		return "Intake System";
	}

	@Override
	public void update() {
		isOverride = override.get();
		joyVal = joystick.get();
		if (IRSensors.get() && !isOverride && joyVal >= 0) {
			motor.set(-.15);
		} else {
			motor.set(joyVal);
		}
	}

	@Override
	public void init() {

	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(motor.getWatchable("intake voltage"), IRSensors.getInfo("has ball"),
				override.getInfo("manual override"));
	}

}
