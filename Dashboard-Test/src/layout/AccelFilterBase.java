package layout;

/**
 * A basic interface for different acceleration profiles.
 *
 * Calculates the kinematic physics of a system by determining distance remaining and maximum, intended, and current velocity and acceleration.
 */
abstract class AccelFilterBase {
	public double currPos;
	public double currVel;
	public double currAcc;

	public AccelFilterBase(double currPos, double currVel, double currAcc) {
		this.currPos = currPos;
		this.currVel = currVel;
		this.currAcc = currAcc;
	}

	// Getter functions
	double GetCurrPos() {
		return currPos;
	}

	double GetCurrVel() {
		return currVel;
	}

	double GetCurrAcc() {
		return currAcc;
	}

	// Recalculate the system
	abstract void calcSystem(double distance_to_target, double v, double goal_v, double max_a, double max_v, double dt);

	public void UpdateVals(double acc, double dt) {
		currAcc = acc;
		currVel += currAcc;
		currPos += currVel * dt;
		currAcc /= dt;
	}

}