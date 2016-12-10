package com.team1389.motion_profile;

public class ConstantAccelProfile extends MotionProfile {
	public Kinematics full;

	public ConstantAccelProfile(double a, double S, double Vo) {
		this(new Kinematics(Vo, Double.NaN, Double.NaN, a, S));
	}

	public ConstantAccelProfile(Kinematics kinematics) {
		this.full = kinematics;
	}

	@Override
	public double providePosition(double time) {
		return getCurrentKinematics(time).S;
	}

	@Override
	public double provideVelocity(double time) {
		return getCurrentKinematics(time).V;
	}

	@Override
	public double provideAcceleration(double time) {
		return full.a;
	}

	@Override
	public double getDuration() {
		return full.t;
	}

	private Kinematics getCurrentKinematics(double time) {
		return new Kinematics(full.Vo, Double.NaN, time, full.a, Double.NaN);
	}
}
