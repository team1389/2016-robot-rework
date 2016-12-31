package com.team1389.motion_profile;

/**
 * This class represents the theoretical path of a particle (or robot). At every time, the position, velocity, and acceleration
 * are well defined. 
 * @author Josh
 *
 */
public abstract class MotionProfile {
	
	/**
	 * @return The total time the motion will take
	 */
	public abstract double getDuration();
	
	/**
	 * Called by {@link #providePosition(double) providePosition} to ensure that time <= duration.
	 * @param time The time to get the position at
	 * @return The position at the inputed time
	 */
	protected abstract double providePosition(double time);


	/**
	 * Called by {@link #provideVelocity(double) provideVelocity} to ensure that time <= duration.
	 * @param time The time to get the velocity at
	 * @return The velocity at the inputed time
	 */
	protected abstract double provideVelocity(double time);


	/**
	 * Called by {@link #provideAcceleration(double) provideAcceleration} to ensure that time <= duration.
	 * @param time The time to get the acceleration at
	 * @return The acceleration at the inputed time
	 */
	protected abstract double provideAcceleration(double time);

	/**
	 * @param time Time to get position at, should be <= duration, however behavior is well defined if this is not the case
	 * @return Position at the requested time, the position at the end if time > duration
	 */
	public double getPosition(double time) {
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return providePosition(getDuration());
		} else {
			return providePosition(time);
		}
	}

	/**
	 * @param time Time to get velocity at, should be <= duration, however behavior is well defined if this is not the case
	 * @return Velocity at the requested time, 0 if time > duration
	 */
	public double getVelocity(double time) {
		double velocity = provideVelocity(time);
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return 0;
		} else {
			return velocity;
		}
	}

	/**
	 * @param time Time to get velocity at, should be <= duration, however behavior is well defined if this is not the case
	 * @return Velocity at the requested time, 0 if time > duration
	 */
	public double getAcceleration(double time) {
		double power = provideAcceleration(time);
		if (time > getDuration()) {
			System.out.println("warning: in MotionProfile, time is called greater than duration");
			return 0;
		} else {
			return power;
		}
	}

	/**
	 * @param time A time (greater than 0) to check if the motion is finished at
	 * @return True if time is greater than duration, false otherwise
	 */
	public boolean isFinished(double time) {
		return time > getDuration();
	}
}
