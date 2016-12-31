package com.team1389.motion_profile;

public class ConstantAccelProfile extends MotionProfile {
	public SimpleKinematics full;
	private int inverted = 1; //-1 if it is inverted

	public ConstantAccelProfile(double a, double S, double Vo) {
		this(new SimpleKinematics(Vo, Double.NaN, Double.NaN, a, S));
	}
	
	public ConstantAccelProfile(double a, double S, double Vo, boolean inverted) {
		this(new SimpleKinematics(Vo, Double.NaN, Double.NaN, a, S));
		
	}

	public ConstantAccelProfile(SimpleKinematics kinematics, boolean inverted) {
		this.full = kinematics;
		if(inverted){
			this.inverted = -1;
		}
	}
	public ConstantAccelProfile(SimpleKinematics kinematics) {
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

	private SimpleKinematics getCurrentKinematics(double time) {
		return new SimpleKinematics(full.Vo, Double.NaN, time, full.a, Double.NaN);
	}
}
