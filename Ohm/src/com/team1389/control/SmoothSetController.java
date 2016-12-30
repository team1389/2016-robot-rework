package com.team1389.control;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.ProfileUtil;

public class SmoothSetController extends MotionProfileController {
	private double maxAccel, maxDecel, maxVel;

	private double setpoint;

	public SmoothSetController(double kP, double kI, double kD, double maxAccel, double maxDecel, double maxVel,
			RangeIn<Position> source, RangeIn<Speed> vel, RangeOut<Percent> output) {
		super(kP, kI, kD, source, vel, output);
		this.maxAccel=maxAccel;
		this.maxDecel=maxDecel;
		this.maxVel=maxVel;
	}

	@Override
	public void setTarget(double target) {
		if (this.setpoint != target) {
			this.setpoint = target;
			double err = target - pos.get();
			followProfile(ProfileUtil.trapezoidal(err, vel.get(), maxAccel, maxDecel, maxVel));
			System.out.println(err);
		}
	}

	public RangeOut<Position> getTargetSetter() {
		return new RangeOut<Position>(this::setTarget, pos.min(), pos.max());
	}

}
