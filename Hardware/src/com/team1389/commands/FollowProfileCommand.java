package com.team1389.commands;

import com.team1389.commands.command_base.Command;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.motion_profile.MotionProfile;

public class FollowProfileCommand extends Command{
	MotionProfile profile;
	Timer timer;
	RangeOut<?> out;
	double initialPos;
	RangeIn<Position> pos;
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out) {
		this(profile,out,0);
	}
	public FollowProfileCommand(MotionProfile profile, RangeOut<Position> out,double initialPos){
		this.profile = profile;
		this.out=out;
		timer = new Timer();
		this.initialPos=initialPos;
	}
	public FollowProfileCommand(MotionProfile profile,RangeOut<Position> out,RangeIn<Position> initial){
		this(profile,out);
		this.pos=initial;
	}
	
	@Override
	public void initialize() {
		if(pos!=null){
		initialPos=pos.get();
		}
		timer.zero();
		System.out.println("zeroing");
	}

	@Override
	public boolean execute() {
		double position = profile.getPosition(timer.get());
		System.out.println(position);
		System.out.println(initialPos);
		out.set(Math.abs(initialPos+position));
		return timer.get() >= profile.getDuration();
	}
	public String toString(){
		return "hi";
	}
}
