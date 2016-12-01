package com.team1389.auto.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConfiguration;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Value;

/**
 * this commmand turns an actuator a given angle
 * 
 * @author amind
 *
 * @param <O> the ouput type
 */
public class TurnAngleCommand<O extends Value> extends Command {
	SynchronousPIDController<O, Angle> pid;
	double angle;
	double tolerance;
	boolean isAbsolute;

	/**
	 * 
	 * @param angle the angle to turn
	 * @param isAbsoluteAngle whether the given angle is an absolute angle, or relative to the acuator's starting angle
	 * @param tolerance the tolerance around the target angle in degrees
	 * @param angleVal an angle input that represents the actuator's angle
	 * @param output an output that control's the actuator's movement
	 * @param turnPID the pid constants for the angle turning controller
	 */
	public TurnAngleCommand(double angle, boolean isAbsoluteAngle, double tolerance, RangeIn<Angle> angleVal,
			RangeOut<O> output, PIDConfiguration turnPID) {
		pid = new SynchronousPIDController<O, Angle>(turnPID, angleVal, output);
		this.angle = angle;
		this.tolerance = tolerance;
		this.isAbsolute = isAbsoluteAngle;
	}
	/**
	 * assumes angle is relative to actuator's starting angle
	 * @param angle the angle to turn
	 * @param tolerance the tolerance around the target angle in degrees
	 * @param angleVal an angle input that represents the actuator's angle
	 * @param output an output that control's the actuator's movement
	 * @param turnPID the pid constants for the angle turning controller
	 */
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
