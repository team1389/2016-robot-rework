package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.LatchedDigitalInput;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.valueTypes.Angle;
import com.team1389.hardware.valueTypes.Percent;
import com.team1389.system.System;
import com.team1389.watch.Info;

public class TurretSystem extends System {
	PercentOut voltRange;
	RangeIn<Percent> joystick;
	public double joyVal;

	RangeIn<Angle> turretAngle;
	
	LatchedDigitalInput zeroButton;
	boolean toZero;
	public TurretSystem(PercentOut voltageSystem, PercentIn joystickIn,RangeIn<Angle> turretAngle,LatchedDigitalInput zeroButton) {
		this.voltRange = voltageSystem;
		this.joystick = joystickIn.scale(.2);
		this.turretAngle=turretAngle;
		this.zeroButton=zeroButton;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Info[] getInfo() {
		return null;
	}

	@Override
	public void init() {

	}

	@Override
	public void updateInputMode() {
		voltRange.set(joyVal);
	}

	@Override
	public void getInput() {
		joyVal = joystick.get();
		
	}

}
