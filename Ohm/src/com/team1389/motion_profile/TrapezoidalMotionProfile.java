package com.team1389.motion_profile;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrapezoidalMotionProfile extends MotionProfile {
	double tAcc;
	double tCruise;
	double tDec;
	double accel;
	double decel;
	double maxSpeed;
	double v0;
	double xAcc;
	double xDec;
	double xCruise;

	public TrapezoidalMotionProfile(double v0, double dx, double accel, double decel, double vCruise) {
		tAcc = (vCruise - v0) / accel; //calculate the time to reach vCruise, assuming there is enough distance in the profile to do so
		tDec = (vCruise) / decel; //calculate the time to decelerate from vCruise to 0
		this.accel = accel;
		this.v0 = v0;
		this.decel = decel;
		xAcc = tAcc * (vCruise + v0) / 2; //calculate distance needed to reach vCruise
		xDec = tDec * (vCruise) / 2; //calculate distance needed to decelerate from vCruise

		if (xAcc + xDec >= Math.abs(dx)) {
			//if this block is entered, the requested distance to travel is not enough for the robot to reach vCruise, so the profile will be triangular
			System.out.println("can't hit vCruise");
			
			//use quadratic forumla to calculate the time to decelerate from the max speed reached (not vCruise)
			double a = (decel * decel / accel) + decel;
			double b = 2 * v0 * decel / accel;
			double c = -2 * dx-2*v0*v0/accel;
			tDec = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
			
			tAcc = (decel * tDec + v0) / accel; //calculate time to accelerate to the new max speed
			maxSpeed = v0+accel*tAcc; //calculate the new max speed
			tCruise = 0; //since we won't reach vCruise, time spent moving at vCruise is 0
			xAcc = tAcc * (maxSpeed + v0) / 2; //calculate the distance spent accelerating
			xDec = tDec * maxSpeed / 2; //calculate distance spent decelerating
		} else {
			//if this block is entered, the robot is able to reach vCruise in the requested distance, so we make a trapezoidal profile
			System.out.println("can hit vCruise");
			xCruise = Math.abs(dx) - xAcc - xDec; //calculate distance travelled at vCruise
			maxSpeed = vCruise; //max speed reached is capped at vCruise
			tCruise = 3 * dx / (2 * vCruise) - tAcc - tDec; //calculate time spent moving at vCruise
		}
		tCruise = xCruise / vCruise;
	}

	@Override
	public double getDuration() {
		return tCruise + tDec + tAcc; //the time spent moving is the sum of time spent in each piece of the trapezoid
	}

	@Override
	protected double providePosition(double time) {
		SmartDashboard.putNumber(this.hashCode() + "", provideVelocity(time));
		if (time <= tAcc) {
			double xSoFar = v0 * time + (accel * time * time / 2);
			return xSoFar;
		} else if (time < tCruise + tAcc) {
			double tCruiseSoFar=time - tAcc; //calculate the time spent in the middle section of the trapezoid
			double xCruise=maxSpeed *tCruiseSoFar; //calculate distance travelled at vCruise so far
			return xAcc + xCruise; //total distance travelled
		} else if (time <= tAcc + tCruise + tDec) {
			double tDec = time - tAcc - tCruise; //calculate time spent in the final section of the trapezoid
			double xDec = maxSpeed * tDec - decel * tDec * tDec / 2; //calculate the distance travelled while decelerating so far
			xDec=(Math.pow(maxSpeed,2)-Math.pow(provideVelocity(time),2))/(-2*decel);	//alternative eqn for xDec
			return xAcc + xCruise + xDec; //total distance travelled
		} else {
			System.out.println("outside duration of profile");
			return 0;
		}
	}

	@Override
	protected double provideVelocity(double time) {

		if (time <= tAcc) {
			return v0 + accel * time; 
		} else if (time < tCruise + tAcc) {
			return maxSpeed;
		} else if (time <= tAcc + tCruise + tDec) {
			return maxSpeed - decel * (time - tAcc - tCruise);
		} else {
			System.out.println("outside duration of profile");
			return 0;
		}
	}

	@Override
	protected double provideAcceleration(double time) {
		if (time <= tAcc) {
			return accel;
		} else if (time < tCruise + tAcc) {
			return 0;
		} else if (time <= tAcc + tCruise + tDec) {
			return decel;
		} else {
			System.out.println("outside duration of profile");
			return 0;
		}
	}
}
