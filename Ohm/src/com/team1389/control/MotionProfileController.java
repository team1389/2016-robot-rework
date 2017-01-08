package com.team1389.control;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.MotionProfile;
import com.team1389.util.Timer;

/**
 * a controller that uses PID to follow motion profiles
 * 
 * @author amind
 *
 */
public class MotionProfileController extends SynchronousPIDController<Percent, Position> {
	MotionProfile following;
	double startPos;
	RangeIn<Speed> vel;
	Timer timer;

	/**
	 * @param kP the proportional gain for the PID controller
	 * @param kI the integral gain for the PID controller
	 * @param kD the derivative gain for the PID controller
	 * @param source a position input stream
	 * @param vel a speed input stream
	 * @param output a percent output
	 */
	public MotionProfileController(double kP, double kI, double kD, RangeIn<Position> source, RangeIn<Speed> vel,
			RangeOut<Percent> output) {
		super(kP, kI, kD, source, output);
		this.vel = vel;
		startPos = 0;
		timer = new Timer();
	}

	/**
	 * stores the given profile to pursue <br>
	 * <em>NOTE</em>: the controller must be updated periodically to follow the profile as expected
	 * 
	 * @param prof the profile to follow
	 */
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
