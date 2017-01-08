package simulation.motor;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.Timer;

public abstract class RotationSim {
	private static final double FRICTION_TORQUE = .1;
	protected double theta = 0; // current angle
	protected double omega = 0; // current rate of rotation (rad/sec)
	protected double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private Timer timer;

	public RotationSim() {
		timer = new Timer();
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, this::getPositionDegrees, 0, 360);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, this::getOmegaDegrees, 0, 360);
	}

	public void update() {
		double dt = timer.getSinceMark();
		timer.mark();
		alpha = calculateAlpha();
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
	}

	private double getPosition() {
		return theta;
	}

	private double getPositionDegrees() {
		return Math.toDegrees(getPosition());
	}

	private double getOmega() {
		return omega;
	}

	private double getOmegaDegrees() {
		return Math.toDegrees(getOmega());
	}

	protected double getFrictionTorque() {
		return -Math.signum(omega) * FRICTION_TORQUE;
	}

	protected double calculateAlpha() {
		return getNetTorque() / getMoment();
	}

	protected abstract double getNetTorque();

	protected abstract double getMoment();

}
