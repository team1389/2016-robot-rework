package com.team1389.motion_profile;

public abstract class MotionProfile {
	public abstract double getDuration();

	protected abstract double providePosition(double time);

	protected abstract double provideVelocity(double time);

	protected abstract double provideAcceleration(double time);

	public double getPosition(double time) {
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return providePosition(getDuration());
		} else {
			return providePosition(time);
		}
	}

	public double getVelocity(double time) {
		double power = provideVelocity(time);
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return 0;
		} else {
			return power;
		}
	}

	public double getAcceleration(double time) {
		double power = provideAcceleration(time);
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return 0;
		} else {
			return power;
		}
	}

	public boolean isFinished(double time) {
		return time > getDuration();
	}
}
