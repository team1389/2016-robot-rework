package com.team1389.auto.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.motion_profile.MotionProfile;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This command moves an actuator along a {@link MotionProfile}
 * @author amind
 */
public class FollowProfileCommand extends Command {
	private static final double DEFAULT_PERCENT_TOLERANCE = .5;
	MotionProfile profile;
	Timer timer;
	RangeOut<?> out;
	double initialPos;
	RangeIn<Position> feedback;
	double percentTolerance;

	/**
	 * @param profile the profile to follow
	 * @param out the position output for the acuator that will follow the given profile
	 * @param feedback a position input that provides the position of the actuator
	 * @param percentTolerance the tolerance
	 */
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out, RangeIn<Position> feedback,
			double percentTolerance) {
		this.profile = profile;
		this.out = out;
		timer = new Timer();
		this.feedback = feedback;
		this.percentTolerance = percentTolerance;
	}

	/**
	 * @param profile the profile to follow
	 * @param out the position output for the acuator that will follow the given profile
	 * @param feedback a position input that provides the position of the actuator
	 */
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out, RangeIn<Position> feedback) {
		this(profile, out, feedback, DEFAULT_PERCENT_TOLERANCE);
	}

	@Override
	public void initialize() {
		initialPos = feedback.get();
		timer.zero();
	}

	@Override
	public boolean execute() {
		// TODO use the percent tolerance to determine when the command should exit
		double profilePosition = profile.getPosition(timer.get());
		double setpoint = initialPos + profilePosition;
		out.set(setpoint);
		double currentPosition = feedback.get();
		double error = setpoint - currentPosition;
		SmartDashboard.putNumber("error", error);

		return timer.get() >= profile.getDuration();
	}
}
