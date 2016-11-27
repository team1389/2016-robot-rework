package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.System;
import com.team1389.watch.BooleanInfo;
import com.team1389.watch.Info;

public class IntakeSystem extends System {
	PercentIn joystick;
	PercentOut motor;
	DigitalInput IRSensors;
	DigitalInput override;

	boolean isOverride;
	double joyVal;

	public IntakeSystem(PercentOut motor, PercentIn joystick, DigitalInput IRSensors, DigitalInput override) {
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
	public Info[] getInfo() {
		return new Info[] { new BooleanInfo("Has Ball", () -> {
			return IRSensors.get();
		}) };
	}

	@Override
	public void getInput() {
		isOverride = override.get();
		joyVal = joystick.get();
	}

	@Override
	public void defaultUpdate() {
		if (IRSensors.get() && !isOverride) {
			motor.set(-.15);
		} else {
			motor.set(joyVal);
		}
	}

	@Override
	public void init() {

	}

}
