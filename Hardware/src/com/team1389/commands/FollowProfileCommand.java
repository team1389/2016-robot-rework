package com.team1389.commands;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.motion_profile.MotionProfile;

public class FollowProfileCommand implements Command{
	MotionProfile profile;
	Timer timer;
	RangeOut<?> out;
	double initialPos;
	
	public FollowProfileCommand(MotionProfile profile, RangeOut<?> out) {
		this(profile,out,0);
	}
	public FollowProfileCommand(MotionProfile profile, RangeOut<?> out,double initialPos){
		this.profile = profile;
		this.out=out;
		timer = new Timer();
		this.initialPos=initialPos;
	}
	
	@Override
	public void init() {
		timer.zero();
	}

	@Override
	public boolean execute() {
		double position = profile.getPosition(timer.get());
		System.out.println(position);
		System.out.println(initialPos);
		out.set(Math.abs(initialPos+position));
		return timer.get() >= profile.getDuration();
	}
}
