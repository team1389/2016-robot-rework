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
		accel *= Math.signum(dx);
		decel *= -Math.signum(dx);
		vCruise *= Math.signum(dx);
		setupConstants(v0, dx, accel, decel, vCruise);
	}

	private void setupConstants(double v0, double dx, double accel, double decel, double vCruise) {
		tAcc = (vCruise - v0) / accel; // calculate the time to reach vCruise, assuming there is enough distance in the profile to do so
		tDec = -vCruise / decel; // calculate the time to decelerate from vCruise to 0
		this.accel = accel;
		this.v0 = v0;
		this.decel = decel;
		xAcc = ((vCruise * vCruise) - (v0 * v0)) / (2 * accel); // calculate distance needed to reach vCruise
		System.out.println("decel " + decel + " " + v0);
		xDec = -(vCruise * vCruise) / (2 * decel); // calculate distance needed to decelerate from vCruise
		System.out.println("x's " + xAcc + " " + xDec + " " + dx);

		if (xAcc + xDec >= dx) {
			// if this block is entered, the requested distance to travel is not enough for the robot to reach vCruise, so the profile will be triangular
			System.out.println("can't hit vCruise");
			xAcc = (-(v0 * v0) - 2 * decel * dx) / (2 * accel - 2 * decel);
			xDec = dx - xAcc;
			xCruise = 0;
			maxSpeed = Math.signum(dx) * Math.sqrt((v0 * v0) + 2 * accel * xAcc);
			tAcc = (maxSpeed - v0) / accel;
			System.out.println(tAcc);
			tDec = -maxSpeed / decel;
		} else {
			System.out.println("can hit vCruise");
			xCruise = Math.abs(dx) - xAcc - xDec; // calculate distance travelled at vCruise
			maxSpeed = vCruise; // max speed reached is capped at vCruise
			tCruise = 3 * dx / (2 * vCruise) - tAcc - tDec; // calculate time spent moving at vCruise
		}
		double xToZero = v0 * v0 / (2 * accel);
		if(xToZero-dx>0){
			accel=this.decel;
			decel=this.accel;
			this.decel=decel;
			this.accel=accel;
		}
		if (dx * v0 < 0) {
			System.out.println("overshoot time");
			// if this block is entered, the current v0 is moving away from the target, so v function will have to pass through 0 to arrive at x
				double ddx = xToZero - dx;
				xAcc = xToZero + ((-2 * decel * ddx) / (2 * accel - 2 * decel));
				System.out.println(tDec);
				xCruise = 0;
				double extraXAcc=xAcc-xToZero;
				xDec = - extraXAcc;
				System.out.println("xDec "+xDec);
				tDec = Math.sqrt(2 * xDec / decel);
				maxSpeed=-Math.sqrt(2*accel*extraXAcc);
				double tExtra=maxSpeed/accel;
				tAcc =tExtra+(-v0 / accel);
				System.out.println("overshoot x's " + xAcc + " " + xDec + " " + dx);
			// if(xToZero>dx);

		}
		tCruise = xCruise / vCruise;
	}

	@Override
	public double getDuration() {
		return tCruise + tDec + tAcc; // the time spent moving is the sum of time spent in each piece of the trapezoid
	}

	@Override
	protected double providePosition(double time) {
		SmartDashboard.putNumber(this.hashCode() + "", provideVelocity(time));
		if (time <= tAcc) {
			double xSoFar = v0 * time + (accel * time * time / 2);
			xAcc=xSoFar;
			return xSoFar;
		} else if (time < tCruise + tAcc) {
			double tCruiseSoFar = time - tAcc; // calculate the time spent in the middle section of the trapezoid
			double xCruise = maxSpeed * tCruiseSoFar; // calculate distance travelled at vCruise so far
			return xAcc + xCruise; // total distance travelled
		} else if (time <= tAcc + tCruise + tDec) {
			double tDec = time - tAcc - tCruise; // calculate time spent in the final section of the trapezoid
			double xDec = maxSpeed * tDec + (decel * tDec * tDec / 2); // calculate the distance travelled while decelerating so far
			// xDec=(Math.pow(maxSpeed,2)-Math.pow(provideVelocity(time),2))/(-2*decel); //alternative eqn for xDec
			System.out.println(xDec);
			return xAcc + xCruise + xDec; // total distance travelled
		} else {
			SmartDashboard.putBoolean("test", true);
			System.out.println("hit the cap motion");
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
