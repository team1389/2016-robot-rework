package com.team1389.motion_profile;

public class ProfileUtil {
	public static MotionProfile generate(double dx, double Vo, double maxAccel, double maxDecel, double maxVel) {
		maxAccel *= Math.signum(dx);
		maxDecel *= -Math.signum(dx);
		maxVel *= Math.signum(dx);
		Kinematics accelSegment = new Kinematics(Vo, maxVel, Double.NaN, maxAccel, Double.NaN);
		Kinematics decelSegment = new Kinematics(maxVel, 0, Double.NaN, maxDecel, Double.NaN);
		double diff = dx - (accelSegment.S + decelSegment.S);
		System.out.println(decelSegment);
		if (diff*Math.signum(dx) > 0) {
			return combine(new ConstantAccelProfile(accelSegment), new ConstantAccelProfile(0, diff, maxVel),
					new ConstantAccelProfile(decelSegment));
		} else {
			System.out.println("can't hit vMax");
			double xAcc = (-(Vo * Vo) - 2 * maxDecel * dx) / (2 * maxAccel - 2 * maxDecel);
			double xDec = dx - xAcc;
			accelSegment = new Kinematics(Vo, Double.NaN, Double.NaN, maxAccel, xAcc);
			decelSegment = new Kinematics(Double.NaN, 0, Double.NaN, maxDecel, xDec);
			return combine(new ConstantAccelProfile(accelSegment), new ConstantAccelProfile(decelSegment));
		}
	}

	public static MotionProfile combine(MotionProfile... motionProfiles) {
		MotionProfile combined = null;
		for (MotionProfile motionProfile : motionProfiles) {
			if (combined == null) {
				combined = motionProfile;
			} else {
				combined = new CombinedProfile(combined, motionProfile);
			}
		}
		return combined;
	}

	public static class CombinedProfile extends MotionProfile {
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
				return profile2.providePosition(time - profile.getDuration())
						+ profile.getPosition(profile.getDuration());
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
}
