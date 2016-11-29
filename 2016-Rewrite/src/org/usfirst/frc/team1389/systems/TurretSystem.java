package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.valueTypes.Percent;
import com.team1389.system.System;
import com.team1389.watch.Info;

public class TurretSystem extends System {
	PercentOut voltRange;
	public double joyVal;
	RangeIn<Percent> joystick;

	public TurretSystem(PercentOut voltageSystem, PercentIn joystickIn) {
		this.voltRange = voltageSystem;
		this.joystick = joystickIn.scale(.2);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Info[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {

	}

	@Override
	public void defaultUpdate() {
	voltRange.set(joyVal);	
	}

	@Override
	public void getInput() {
		joyVal = joystick.get();

	}

}
