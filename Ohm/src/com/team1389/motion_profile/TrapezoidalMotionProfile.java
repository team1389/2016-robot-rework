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
		tAcc = (vCruise - v0) / accel;
		tDec = (vCruise) / decel;
		this.accel = accel;
		this.v0 = v0;
		this.decel = decel;
		xAcc = tAcc * (vCruise + v0) / 2;
		xDec = tDec * (vCruise) / 2;

		if (xAcc + xDec >= Math.abs(dx)) {
			System.out.println("can't hit max vel");
			double a = (decel * decel / accel) + decel;
			double b = 2 * v0 * decel / accel;
			double c = -2 * dx;
			tDec = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
			tAcc = (decel * tDec + v0) / accel;
			maxSpeed = 2 * dx / (tAcc + tDec);
			tCruise = 0;
			xAcc = tAcc * (maxSpeed + v0) / 2;
			xDec = tDec * maxSpeed / 2;
			System.out.println(maxSpeed);
		} else {
			System.out.println("hit max vel");
			xCruise = Math.abs(dx) - xAcc - xDec;
			maxSpeed = vCruise;
			tCruise = 3 * dx / (2 * vCruise) - tAcc - tDec;
		}
		tCruise = xCruise / vCruise;
	}

	@Override
	public double getDuration() {
		return tCruise + tDec + tAcc;
	}

	@Override
	protected double providePosition(double time) {
		SmartDashboard.putNumber(this.hashCode() + "", provideVelocity(time));
		if (time <= tAcc) {
			return v0 * time + (accel * time * time / 2);
		} else if (time < tCruise + tAcc) {
			double tCruise=time - tAcc;
			double xCruise=maxSpeed *tCruise;
			return xAcc + xCruise;
		} else if (time <= tAcc + tCruise + tDec) {
			double tDec = time - tAcc - tCruise;
			double xDec = maxSpeed * tDec - decel * tDec * tDec / 2;
			xDec=(Math.pow(provideVelocity(time),2)-Math.pow(maxSpeed,2))/(-2*decel);	
			SmartDashboard.putNumber("dec", xDec);
			return xAcc + xCruise + xDec;
		} else {
			System.out.println("fuck you");
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
			System.out.println("fuck you");
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
			System.out.println("fuck you");
			return 0;
		}
	}
}
