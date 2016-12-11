package org.usfirst.frc.team1389.systems;

import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.System;
import com.team1389.watch.Watchable;

public class TurretSystem extends System {
	// TODO think about pollable implementation
	PercentOut voltRange;

	RangeIn<Percent> joystick;
	public double joyVal;

	RangeIn<Angle> turretAngle;

	DigitalIn zeroButton;
	boolean toZero;

	public TurretSystem(PercentOut voltageSystem, RangeIn<Angle> turretAngle, PercentIn joystickIn,
			DigitalIn zeroButton) {
		this.voltRange = voltageSystem;
		this.joystick = joystickIn.scale(.2);
		this.turretAngle = turretAngle;
		this.zeroButton = zeroButton;
	}

	@Override
	public String getName() {
		return "Turret subsystem";
	}

	@Override
	public void init() {

	}

	public void zero() {
		schedule(new TurnAngleCommand<Percent>(0, true, 2, turretAngle, voltRange, new PIDConstants(.07, 0, .15)));
	}

	@Override
	public void defaultUpdate() {
		voltRange.set(joyVal);
		if (toZero) {
			zero();
		}
	}

	@Override
	public void getInput() {
		joyVal = joystick.get();
		toZero = zeroButton.get();
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { turretAngle.getWatchable("turret Angle") };
	}

}
