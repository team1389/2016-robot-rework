package com.team1389.motion_profile;

public class ContantSpeedMotionProfile extends MotionProfile{
	
	double duration, speed;
	
	public ContantSpeedMotionProfile(double duration, double speed){
		this.duration = duration;
		this.speed = speed;
	}

	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public double providePosition(double time) {
		return time * speed;
	}

	@Override
	public double provideVelocity(double time) {
		return speed;
	}

	@Override
	public double provideAcceleration(double time) {
		return 0;
	}

}
