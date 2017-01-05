package motor_sim;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.RangeUtil;

public class SimpleMotorSim{
	private static final double FRICTION_TORQUE = 1.25;
	private static final double FREE_MOMENT = 5.5e-7;
	Motor motor;
	private double theta = 0; // current angle
	private double omega = 0; // current rate of rotation (rad/sec)
	private double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private Timer timer;

	public SimpleMotorSim(Motor motor) {
		this.motor=motor;
		timer=new Timer();
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setVoltage);
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, this::getPositionDegrees, 0, 360);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, this::getOmegaDegrees, 0, 360);
	}

	public void setVoltage(double voltage) {
		double limVoltage = RangeUtil.limit(voltage, -1, 1);
		motor.setPercentVoltage(limVoltage);
	}

	public void update() {
		double dt = timer.get();
		alpha = calculateAlpha();
		System.out.println(alpha*dt);
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
		timer.zero();
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

	private double calculateAlpha() {
		return (motor.getTorque(omega) - (Math.signum(omega) * FRICTION_TORQUE)) / FREE_MOMENT;
	}

}
