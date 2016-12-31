package com.team1389.motion_profile;

public class ProfileUtil {

	public static MotionProfile trapezoidal(double dx, double Vo, double maxAccel, double maxDecel, double maxSpeed) {
		// Make sure these values are positive
		maxDecel = Math.abs(maxDecel);
		maxAccel = Math.abs(maxAccel);
		maxSpeed = Math.abs(maxSpeed);

		boolean inverted = false;
		if (dx < 0) {
			Vo *= -1;
			dx *= -1;
			inverted = true;
		}
		// From here on out is is assumed dx is positive

		// If Vo is less than 0 (i.e. we are going in the opposite direction of our dx) immediately slow to 0 and then calculated
		// what to do next (recursivly)
		if (Vo < 0) {
			SimpleKinematics slowDown = new SimpleKinematics(Vo, 0, Double.NaN, maxDecel, Double.NaN);
			return combine(new ConstantAccelProfile(slowDown, inverted),
					trapezoidal((inverted ? -1 : 1) * (dx - slowDown.X), 0, maxAccel, maxDecel, maxSpeed));
		}

		// From here on out, it is assumed vo >= 0
		SimpleKinematics accelSegment = new SimpleKinematics(Vo, maxSpeed, Double.NaN, maxAccel, Double.NaN);
		SimpleKinematics decelSegment = new SimpleKinematics(maxSpeed, 0, Double.NaN, -maxDecel, Double.NaN);

		double diff = dx - (accelSegment.X + decelSegment.X);
		if (diff < 0) {
			SimpleKinematics testDecel = new SimpleKinematics(Vo, 0, Double.NaN, -maxAccel, Double.NaN);
			if (testDecel.X > dx /* i.e. we cant stop in time and have to backtrack */) {
				double vMax = -calculateTopSpeed(testDecel.X - dx, 0, maxAccel, maxDecel);
				accelSegment = new SimpleKinematics(0, vMax, Double.NaN, -maxAccel, Double.NaN);
				decelSegment = new SimpleKinematics(vMax, 0, Double.NaN, maxDecel, Double.NaN);
				return combine(new ConstantAccelProfile(testDecel, inverted),
						new ConstantAccelProfile(accelSegment, inverted),
						new ConstantAccelProfile(decelSegment, inverted));
			}

			double vMax = calculateTopSpeed(dx, Vo, maxAccel, maxDecel);
			accelSegment = new SimpleKinematics(Vo, vMax, Double.NaN, maxAccel, Double.NaN);
			decelSegment = new SimpleKinematics(vMax, 0, Double.NaN, -maxDecel, Double.NaN);
			return combine(new ConstantAccelProfile(accelSegment, inverted),
					new ConstantAccelProfile(decelSegment, inverted));
		} else {
			return combine(new ConstantAccelProfile(accelSegment, inverted),
					/* I'm not 100% sure about this next argument, but apparently it works in all cases. "voodoo magic" */
					new ConstantAccelProfile(0, (inverted ? -1 : 1) * diff, (inverted ? -1 : 1) * maxSpeed, inverted),
					new ConstantAccelProfile(decelSegment, inverted));
		}
	}

	/**
	 * Returns the height of the triangular speed profile. In other words, how fast you can possibly go with constant acceleration and constant deceleration in the given x. It is interpreted as a speed, not a velocity, so it could be negative. However, just assume everything is positive and convert later.
	 * 
	 * @param dx The magnitude of the distance to travel
	 * @param Vo The magnitude of the initial velocity
	 * @param a The acceleration, the right side of the triangle
	 * @param d The deceleration, the left side of the triangle
	 * @return The magnitude of the max velocity of the triangle
	 */
	private static double calculateTopSpeed(double dx, double Vo, double a, double d) {
		a = Math.abs(a);
		d = Math.abs(d);
		dx = Math.abs(dx);
		// For the derivation of this equation, draw out a triangle and think it out.
		// For a hint: use vf ^ 2 = vo^2 + 2ax twice.
		return Math.sqrt((dx + Vo * Vo / 2.0 / a) / (1.0 / 2.0 / a + 1.0 / 2.0 / d));
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
