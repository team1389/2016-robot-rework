package com.team1389.autonomous.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConfiguration;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Value;

public class TurnAngleCommand<O extends Value> extends Command {
	SynchronousPIDController<O, Angle> pid;
	double angle;
	double tolerance;
	boolean isAbsolute;

	public TurnAngleCommand(double angle, boolean isAbsoluteAngle, double tolerance, RangeIn<Angle> angleVal,
			RangeOut<O> output, PIDConfiguration turnPID) {
		pid = new SynchronousPIDController<O, Angle>(turnPID, angleVal, output);
		this.angle = angle;
		this.tolerance = tolerance;
		this.isAbsolute = isAbsoluteAngle;
	}

	public TurnAngleCommand(double angle, double tolerance, RangeIn<Angle> angleVal, RangeOut<O> output,
			PIDConfiguration turnPID) {
		this(angle, false, tolerance, angleVal, output, turnPID);

	}

	@Override
	public void initialize() {
		if (!isAbsolute) {
			angle += pid.getSource().get();
		}
		pid.setSetpoint(angle);
	}

	@Override
	public boolean execute() {
		pid.update();
		return pid.onTarget(tolerance);
	}
}
