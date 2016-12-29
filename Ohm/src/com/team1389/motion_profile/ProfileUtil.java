package com.team1389.motion_profile;

public class ProfileUtil {
	public static MotionProfile generate(double dx, double Vo, double maxAccel, double maxDecel, double maxVel) {
		if (dx == 0) {
			maxAccel *= Math.signum(Vo);
			maxDecel *= -Math.signum(Vo);
			maxVel *= Math.signum(Vo);
		} else {
			maxAccel *= Math.signum(dx);
			maxDecel *= -Math.signum(dx);
			maxVel *= Math.signum(dx);
		}
		
		Kinematics accelSegment = new Kinematics(Vo, maxVel, Double.NaN, maxAccel, Double.NaN);
		Kinematics decelSegment = new Kinematics(maxVel, 0, Double.NaN, maxDecel, Double.NaN);
		double diff = dx - (accelSegment.X + decelSegment.X);
		if (diff * Math.signum(dx) > 0) {
			return combine(new ConstantAccelProfile(accelSegment), new ConstantAccelProfile(0, diff, maxVel),
					new ConstantAccelProfile(decelSegment));
		} else {
			accelSegment = new Kinematics(Vo, 0, Double.NaN, maxDecel, Double.NaN);
			if (dx - accelSegment.X < 0) {
				return combine(new ConstantAccelProfile(accelSegment),
						generate(-accelSegment.X, 0, Math.abs(maxAccel), Math.abs(maxDecel), maxVel));
			}
			double xAcc = (-(Vo * Vo) - 2 * maxDecel * dx) / (2 * maxAccel - 2 * maxDecel);
			double xDec = dx - xAcc;
			accelSegment = new Kinematics(Vo, Double.NaN, Double.NaN, maxAccel, xAcc);
			decelSegment = new Kinematics(Double.NaN, 0, Double.NaN, maxDecel, xDec);
			return combine(new ConstantAccelProfile(accelSegment), new ConstantAccelProfile(decelSegment));
		}
	}

	public static MotionProfile generate2(double dx, double Vo, double maxAccel, double maxDecel, double maxSpeed){
		double originaldx = dx;
		
		maxDecel *= -1;
		boolean inverted = false;
		if(dx < 0){
			Vo *= -1;
			dx *= -1;
			inverted = true;
		}
		
		if(Vo < 0){
			Kinematics slowDown = new Kinematics(Vo, 0, Double.NaN, -maxDecel, Double.NaN);
			//System.out.println(dx - slowDown.X);
			return combine(new ConstantAccelProfile(slowDown, inverted), generate2(/*these should be opposite signs*/ dx - slowDown.X, 0, maxAccel, -maxDecel, maxSpeed));
		}
		
		
		//From here on out, it is assumed vo > 0
		Kinematics accelSegment = new Kinematics(Vo, maxSpeed, Double.NaN, maxAccel, Double.NaN);
		Kinematics decelSegment = new Kinematics(maxSpeed, 0, Double.NaN, maxDecel, Double.NaN);
		
		double diff = dx - (accelSegment.X + decelSegment.X);
		if(diff < 0){
			
			Kinematics testAccel = new Kinematics(Vo, 0, Double.NaN, -maxAccel, Double.NaN);
			//Cant stop in time
			if(testAccel.X > dx){
				double changeX = testAccel.X - dx;
				//I could probably add a method or something for this code and the below code
				//In fact, theres probably a way to do it with kinematics
				double v2 = -Math.sqrt(changeX / (1.0 / 2.0 / maxAccel + 1.0 / 2.0 / (maxDecel * -1.0)));
				accelSegment = new Kinematics(0, v2, Double.NaN, -maxAccel, Double.NaN);
				decelSegment = new Kinematics(v2, 0, Double.NaN, -maxDecel, Double.NaN);
				return combine(new ConstantAccelProfile(testAccel, inverted), 
						new ConstantAccelProfile(accelSegment, inverted), new ConstantAccelProfile(decelSegment, inverted));
			}
			
			//Complicated math, i will explain later, not too important though, just calculated max speed
			//Im like 90% sure its right
			double v2 = Math.sqrt((dx + Vo * Vo / 2.0 / maxAccel) / (1.0 / 2.0 / maxAccel + 1.0 / 2.0 / (maxDecel * -1.0)));
			accelSegment = new Kinematics(Vo, v2, Double.NaN, maxAccel, Double.NaN);
			decelSegment = new Kinematics(v2, 0, Double.NaN, maxDecel, Double.NaN);
			return combine(new ConstantAccelProfile(accelSegment, inverted), new ConstantAccelProfile(decelSegment, inverted));
		}
		else{
			return combine(new ConstantAccelProfile(accelSegment, inverted), new ConstantAccelProfile(0, diff, maxSpeed, inverted),
					new ConstantAccelProfile(decelSegment, inverted));
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
