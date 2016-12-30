package com.team1389.motion_profile;

public class CombinedProfile extends MotionProfile {
	MotionProfile profile;
	MotionProfile profile2;

	public CombinedProfile(MotionProfile profile, MotionProfile profile2) {
		this.profile = profile;
		this.profile2 = profile2;
	}

	@Override
	public double getDuration() {
		return profile.getDuration() + profile2.getDuration();
	}

	@Override
	protected double providePosition(double time) {
		if (time < profile.getDuration()) {
			return profile.providePosition(time);
		} else {
			return profile2.providePosition(time - profile.getDuration()) + profile.getPosition(profile.getDuration());
		}
	}

	@Override
	protected double provideVelocity(double time) {
		if (time < profile.getDuration()) {
			return profile.provideVelocity(time);
		} else {
			return profile2.provideVelocity(time);
		}
	}

	@Override
	protected double provideAcceleration(double time) {
		if (time < profile.getDuration()) {
			return profile.provideAcceleration(time);
		} else {
			return profile2.provideAcceleration(time);
		}
	}

	@Override
	public String toString() {
		return "[" + profile + "," + profile2 + "]";
	}
}
