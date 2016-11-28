package com.team1389.motion_profile;

public class TrapezoidalMotionProfile extends MotionProfile {
	double tAcc;
	double tCruise;
	double tDec;
	double accel;
	double decel;
	double maxSpeed;

	public TrapezoidalMotionProfile(double dx, double accel, double decel, double vCruise) {
		tAcc = vCruise / accel;
		tDec = vCruise / decel;
		this.accel=accel;
		this.decel=decel;
		double xAcc = tAcc * vCruise/2;
		double xDec = tDec * vCruise/2;
		double xCruise;
		
		if (xAcc + xDec > Math.abs(dx)) {
			double t2sq = Math.abs(dx)*2048 / (decel * decel / accel + decel);
			xDec = decel * t2sq/2048;
			tDec = Math.sqrt(t2sq)/32;
			tAcc = tDec * decel / accel;
			maxSpeed = tAcc * accel;
			xCruise = 0;
		} else {
			xCruise = Math.abs(dx) - xAcc - xDec;
			maxSpeed = vCruise;
			tCruise=3*dx/(2*vCruise)-tAcc-tDec;
		}
		tCruise=xCruise/vCruise;
	}

	@Override
	public double getDuration() {
		return tAcc+tCruise+tDec;
	}

	@Override
	protected double providePosition(double time) {
		if (time <= tAcc) {
			return accel*time*time/2;
		} else if (time < tCruise + tAcc) {
			return  accel*tAcc*tAcc/2+ maxSpeed*(time-tAcc);
		} else if (time <= tAcc + tCruise + tDec) {
			double tDec=time-tAcc-tCruise;
			return (accel*tAcc*tAcc/2)+(maxSpeed*(tCruise))+maxSpeed*tDec-decel*tDec*tDec/2;
		} else {
			System.out.println("fuck you");
			return 0;
		}	}

	@Override
	protected double provideVelocity(double time) {

		if (time <= tAcc) {
			return accel * time;
		} else if (time < tCruise + tAcc) {
			return maxSpeed;
		} else if (time <= tAcc + tCruise + tDec) {
			return maxSpeed -decel * (time - tAcc - tCruise);
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
