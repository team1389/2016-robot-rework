package org.usfirst.frc.team1389.systems;

import com.team1389.commands.CommandUtil;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;
import com.team1389.control.PIDSystemCreator;
import com.team1389.control.SynchronousPIDController;
import com.team1389.control.pid_wrappers.input.PIDRangeSource;
import com.team1389.control.pid_wrappers.output.PIDControlledRange;
import com.team1389.hardware.inputs.software.LatchedDigitalInput;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Angle;
import com.team1389.hardware.valueTypes.Percent;
import com.team1389.hardware.valueTypes.Value;
import com.team1389.system.System;
import com.team1389.watch.Info;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class TurretSystem extends System {
	// TODO think about pollable implementation
	PercentOut voltRange;

	RangeIn<Percent> joystick;
	public double joyVal;

	RangeIn<Angle> turretAngle;

	LatchedDigitalInput zeroButton;
	boolean toZero;

	public TurretSystem(PercentOut voltageSystem, PercentIn joystickIn, RangeIn<Angle> turretAngle,
			LatchedDigitalInput zeroButton) {
		this.voltRange = voltageSystem;
		this.joystick = joystickIn.scale(.2);
		this.turretAngle = turretAngle;
		this.zeroButton = zeroButton;
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

	public void zero() {
		PIDConfiguration pid = new PIDConfiguration(new PIDConstants(.07, 0, .15), false, false);
		PIDSource source = new PIDRangeSource<Angle>(turretAngle);
		PIDOutput out = new PIDControlledRange<Percent>(voltRange);
		SynchronousPIDController setter = PIDSystemCreator.makeSynchronousController(pid, source, out);
		RangeOut<Value> setpointSetter = new RangeOut<Value>((double setPoint) -> {
			setter.setSetpoint(setPoint);
		}, -180, 180).getProfiledOut(5, turretAngle.get());
		setter.setInputRange(-180, 180);
		setter.setOutputRange(-1, 1);

		schedule(CommandUtil.createCommand(() -> {
			setpointSetter.set(0);
			return setter.getSetpoint() == 0 && setter.onTargetStable(.5);
		}));
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

}
