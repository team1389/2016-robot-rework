package com.team1389.commands;

import com.team1389.commands.command_base.Command;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.motion_profile.MotionProfile;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FollowProfileCommand extends Command{
	private static final double DEFAULT_PERCENT_TOLERANCE=.5;
	MotionProfile profile;
	Timer timer;
	RangeOut<?> out;
	double initialPos;
	RangeIn<Position> feedback;
	double percentTolerance;
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out,RangeIn<Position> feedback, double percentTolerance){
		this.profile = profile;
		this.out=out;
		timer = new Timer();
		this.feedback=feedback;
		this.percentTolerance=percentTolerance;
	}
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out,RangeIn<Position> feedback){
		this(profile,out,feedback,DEFAULT_PERCENT_TOLERANCE);
	}
	@Override
	public void initialize() {
		initialPos=feedback.get();
		timer.zero();
	}

	@Override
	public boolean execute() {
		double profilePosition = profile.getPosition(timer.get());
		double setpoint=initialPos+profilePosition;
		out.set(setpoint);
		double currentPosition=feedback.get();
		double error=setpoint-currentPosition;
		SmartDashboard.putNumber("error", error);

		return timer.get() >= profile.getDuration();
	}
}
