package com.team1389.motion_profile;

public class ConstantAccellerationMotionProfile extends MotionProfile{
	private static interface DoubleFunction{
		double run(double in);
	}
	
	DoubleFunction position;
	DoubleFunction velocity;
	DoubleFunction acceleration;
	double duration;
	
	
	//black magic: do not touch
	public ConstantAccellerationMotionProfile(double rawDistance, double maxVel, double maxAcc) {
		double distance = Math.abs(rawDistance);
		double multiplyer;
		if (rawDistance < 0){
			multiplyer = -1;
		} else {
			multiplyer = 1;
		}
		double rampDist = maxVel * maxVel / (2 * maxAcc);
		
		if (2 * rampDist <= distance){//trapezoidal motion profile function
			double rampTime = maxVel / maxAcc;
			duration = (distance / maxVel) + (maxVel / maxAcc);
			double rampDownStartTime = duration - rampTime;
			
			double positionAtRampTime = .5 * maxAcc * rampTime * rampTime;
			double positionAtRampStartTime = positionAtRampTime + maxVel * (rampDownStartTime - rampTime);
			position = (t) -> {
				double position;
				if (t <= rampTime){
					position = .5 * maxAcc * t * t;
				} else if (t <= rampDownStartTime) {
					position = positionAtRampTime + maxVel * (t - rampTime);
				} else {
					double timeSince = t - rampDownStartTime;
					position = positionAtRampStartTime + timeSince * maxVel - 0.5 * maxAcc * timeSince * timeSince;
				}
				return multiplyer * position;
			};
			velocity = (t) -> {
				double velocity;
				if (t <= rampTime){
					velocity = t * maxAcc;
				} else if (t <= rampDownStartTime){
					velocity = maxVel;
				} else {
					velocity = maxVel - (maxAcc * (t - rampDownStartTime));
				}
				return velocity * multiplyer;
			};
			acceleration = (t) -> {
				double acceleration;
				if (t <= rampTime){
					acceleration = maxAcc;
				} else if (t <= rampDownStartTime){
					acceleration = 0;
				} else {
					acceleration = -maxAcc;
				}
				return acceleration * multiplyer;
			};
		} else {//triangular motion profile function
			duration = 2 * Math.sqrt(distance / maxAcc);
			double maxActualVel = (maxAcc * duration) / 2;
			double halfTime = duration / 2;
			double positionAtHalfTime = .5 * maxAcc * halfTime * halfTime;
			position = (t) -> {
				double position;
				if (t <= halfTime){
					position = .5 * maxAcc * t * t;
				} else {
					double timeSince = t - halfTime;
					position = positionAtHalfTime + timeSince * maxActualVel  - .5 * maxAcc * timeSince * timeSince;
				}
				return position * multiplyer;
			};
			
			velocity = (t) -> {
				double velocity;
				if (t <= halfTime){
					velocity = t * maxAcc;
				} else {
					velocity = maxActualVel - (maxAcc * (t - halfTime));
				}
				return velocity * multiplyer;
			};
			
			acceleration = (t) -> {
				double acceleration;
				if (t <= halfTime){
					acceleration = maxAcc;
				} else {
					acceleration = -maxAcc;
				}
				return acceleration * multiplyer;
			};
		}
	}

	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public double provideVelocity(double time) {
		return velocity.run(time);
	}
	
	public static void main(String[] args){
		MotionProfile profile = 
				new ConstantAccellerationMotionProfile(-10, 3, 1);
		
		System.out.println("duration: " + profile.getDuration());
		
		System.out.println(profile.getPosition(0));
		System.out.println(profile.getPosition(1));
		System.out.println(profile.getPosition(2));
		System.out.println(profile.getPosition(3));
		System.out.println(profile.getPosition(4));
		System.out.println(profile.getPosition(5));
		System.out.println(profile.getPosition(6));
		System.out.println(profile.getPosition(7));
		System.out.println(profile.getPosition(8));
		System.out.println(profile.getPosition(9));
		System.out.println(profile.getPosition(10));
	}

	@Override
	public double provideAcceleration(double time) {
		return acceleration.run(time);
	}

	@Override
	public double providePosition(double time) {
		return position.run(time);
	}

}
