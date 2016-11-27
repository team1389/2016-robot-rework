package com.team1389.commands;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.motion_profile.MotionProfile;

public class FollowProfileCommand implements Command{
	MotionProfile profile;
	Timer timer;
	RangeOut<?> out;
	
	public FollowProfileCommand(MotionProfile profile, RangeOut<?> out) {
		this.profile = profile;
		this.out=out;
		timer = new Timer();
	}
	
	@Override
	public void init() {
		timer.zero();
	}

	@Override
	public boolean execute() {
		out.set(profile.getPosition(timer.get()));
		return timer.get() >= profile.getDuration();
	}
}
