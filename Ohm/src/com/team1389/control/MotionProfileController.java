package com.team1389.control;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.MotionProfile;

public class MotionProfileController extends SynchronousPIDController<Percent, Position> {
	MotionProfile following;
	double startPos;
	RangeIn<Speed> vel;
	Timer timer;

	public MotionProfileController(double kP, double kI, double kD, RangeIn<Position> source, RangeIn<Speed> vel,
			RangeOut<Percent> output) {
		super(kP, kI, kD, source, output);
		this.vel = vel;
		startPos = 0;
		timer = new Timer();
	}

	public void followProfile(MotionProfile prof) {
		this.following = prof;
		startPos = source.get();
		timer.zero();
	}

	@Override
	public void update() {
		double time = timer.get();
		if (following != null && !following.isFinished(time)) {
			super.setSetpoint(following.getPosition(time) + startPos);
		}
		super.update();
	}

}
