package com.team1389.motion_profile;

public class ProfileUtil {

	public static MotionProfile trapezoidal(double dx, double Vo, double maxAccel, double maxDecel, double maxSpeed) {
		maxDecel *= -1;
		boolean inverted = false;
		if (dx < 0) {
			Vo *= -1;
			dx *= -1;
			inverted = true;
		}

		if (Vo < 0) {
			SimpleKinematics slowDown = new SimpleKinematics(Vo, 0, Double.NaN, -maxDecel, Double.NaN);
			return combine(new ConstantAccelProfile(slowDown, inverted),
					trapezoidal((inverted ? -1 : 1) * (dx - slowDown.X), 0, maxAccel, -maxDecel, maxSpeed));
		}

		// From here on out, it is assumed vo > 0
		SimpleKinematics accelSegment = new SimpleKinematics(Vo, maxSpeed, Double.NaN, maxAccel, Double.NaN);
		SimpleKinematics decelSegment = new SimpleKinematics(maxSpeed, 0, Double.NaN, maxDecel, Double.NaN);

		double diff = dx - (accelSegment.X + decelSegment.X);
		if (diff < 0) {

			SimpleKinematics testAccel = new SimpleKinematics(Vo, 0, Double.NaN, -maxAccel, Double.NaN);
			// Cant stop in time
			if (testAccel.X > dx) {
				double changeX = testAccel.X - dx;
				// I could probably add a method or something for this code and the below code
				// In fact, theres probably a way to do it with kinematics
				double v2 = -Math.sqrt(changeX / (1.0 / 2.0 / maxAccel + 1.0 / 2.0 / (maxDecel * -1.0)));
				accelSegment = new SimpleKinematics(0, v2, Double.NaN, -maxAccel, Double.NaN);
				decelSegment = new SimpleKinematics(v2, 0, Double.NaN, -maxDecel, Double.NaN);
				return combine(new ConstantAccelProfile(testAccel, inverted),
						new ConstantAccelProfile(accelSegment, inverted),
						new ConstantAccelProfile(decelSegment, inverted));
			}

			// Complicated math, i will explain later, not too important though, just calculated max speed
			// Im like 90% sure its right
			double v2 = Math
					.sqrt((dx + Vo * Vo / 2.0 / maxAccel) / (1.0 / 2.0 / maxAccel + 1.0 / 2.0 / (maxDecel * -1.0)));
			accelSegment = new SimpleKinematics(Vo, v2, Double.NaN, maxAccel, Double.NaN);
			decelSegment = new SimpleKinematics(v2, 0, Double.NaN, maxDecel, Double.NaN);
			return combine(new ConstantAccelProfile(accelSegment, inverted),
					new ConstantAccelProfile(decelSegment, inverted));
		} else {
			return combine(new ConstantAccelProfile(accelSegment, inverted),
					new ConstantAccelProfile(0, (inverted ? -1 : 1) * diff, (inverted ? -1 : 1) * maxSpeed, inverted),
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

}
