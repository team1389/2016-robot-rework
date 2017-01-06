package com.team1389.control;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.ProfileUtil;

/**
 * generates trapezoidal motion profiles in real time to direct the controller towards the setpoint within a fixed set of kinematics constraints
 * 
 * @author amind
 *
 */
public class SmoothSetController extends MotionProfileController {
	private double maxAccel, maxDecel, maxVel;

	private double setpoint;

	/**
	 * @param kP the proportional gain for the PID controller
	 * @param kI the integral gain for the PID controller
	 * @param kD the derivative gain for the PID controller
	 * @param maxAccel the max acceleration
	 * @param maxDecel the maximum deceleration
	 * @param maxVel the max velocity
	 * @param source a position input stream
	 * @param vel a speed input stream
	 * @param output a percent output
	 */
	public SmoothSetController(double kP, double kI, double kD, double maxAccel, double maxDecel, double maxVel,
			RangeIn<Position> source, RangeIn<Speed> vel, RangeOut<Percent> output) {
		super(kP, kI, kD, source, vel, output);
		this.maxAccel = maxAccel;
		this.maxDecel = maxDecel;
		this.maxVel = maxVel;
	}

	@Override
	public void setSetpoint(double target) {
		target = clampSetpoint(target);
		if (this.setpoint != target) {
			this.setpoint = target;
			double err = target - source.get();
			followProfile(ProfileUtil.trapezoidal(err, vel.get(), maxAccel, maxDecel, maxVel));
		}
	}

	@Override
	public RangeOut<Position> getSetpointSetter() {
		return new RangeOut<Position>(this::setSetpoint, source.min(), source.max());
	}

}
