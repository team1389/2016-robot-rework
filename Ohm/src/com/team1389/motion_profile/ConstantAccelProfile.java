package com.team1389.motion_profile;

public class ConstantAccelProfile extends MotionProfile {
	public Kinematics full;
	private int inverted = 1; //-1 if it is inverted

	public ConstantAccelProfile(double a, double S, double Vo) {
		this(new Kinematics(Vo, Double.NaN, Double.NaN, a, S));
	}
	
	public ConstantAccelProfile(double a, double S, double Vo, boolean inverted) {
		this(new Kinematics(Vo, Double.NaN, Double.NaN, a, S));
		
	}

	public ConstantAccelProfile(Kinematics kinematics, boolean inverted) {
		this.full = kinematics;
		if(inverted){
			this.inverted = -1;
		}
	}
	public ConstantAccelProfile(Kinematics kinematics) {
		this.full = kinematics;
	}

	@Override
	public double providePosition(double time) {
		return getCurrentKinematics(time).X * inverted;
	}

	@Override
	public double provideVelocity(double time) {
		return getCurrentKinematics(time).V * inverted;
	}

	@Override
	public double provideAcceleration(double time) {
		return full.a * inverted;
	}

	@Override
	public double getDuration() {
		return full.t;
	}

	private Kinematics getCurrentKinematics(double time) {
		return new Kinematics(full.Vo, Double.NaN, time, full.a, Double.NaN);
	}
}
