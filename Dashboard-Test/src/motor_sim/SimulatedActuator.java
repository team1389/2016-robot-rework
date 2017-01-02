package motor_sim;

import java.util.HashSet;
import java.util.Set;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class SimulatedActuator {
	Set<Motor> motors;
	private double gearReduction;
	private Set<Attachment> attachments;
	private double rangeMin, rangeMax;

	private double theta = 0; // current angle
	private double omega = 0; // current rate of rotation (rad/sec)
	private double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private Timer timer;

	public SimulatedActuator(Set<Motor> motors, Set<Attachment> attachments, double gearing) {
		timer = new Timer();
		this.gearReduction = gearing;
		this.attachments = attachments;
		this.motors = motors;
		this.rangeMax = Double.MAX_VALUE;
		this.rangeMin = Double.MIN_VALUE;
	}

	public SimulatedActuator(Motor motor, Attachment attachment, double gearing) {
		this(new HashSet<>(), new HashSet<>(), gearing);
		motors.add(motor);
		attachments.add(attachment);
	}

	public SimulatedActuator(Motor motor) {
		this(motor, Attachment.FREE, 1);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setVoltage);
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, this::getPosition, 0, 2 * Math.PI);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> omega / gearReduction, 0, 2 * Math.PI);
	}

	public void setVoltage(double voltage) {
		motors.forEach(m -> m.setVoltage(voltage));
	}

	public void update() {
		double dt = timer.get();
		System.out.println(getMotorTorque() + " " + getAttachmentTorque());
		alpha = calculateAlpha();
		omega += alpha * dt; // add to velocity
		if ((omega > 0 && getPosition() < rangeMax) || (omega < 0 && getPosition() > rangeMin)) {
			theta += omega * dt; // add to position
		} else {
			omega = 0;
		}
		timer.zero();
	}

	private double getPosition() {
		return theta / gearReduction;
	}

	private double calculateAlpha() {
		double totalTorque = getMotorTorque() * gearReduction + getAttachmentTorque();
		return totalTorque / getAttachmentMoment();
	}

	private double getMotorTorque() {
		return motors.stream().mapToDouble(m -> m.getTorque(omega)).sum();
	}

	private double getAttachmentTorque() {
		return attachments.stream().mapToDouble(a -> a.getAddedTorque(theta)).sum();
	}

	private double getAttachmentMoment() {
		return attachments.stream().mapToDouble(a -> a.moment).sum();
	}

	public void setRangeOfMotion(double min, double max) {
		this.rangeMax = Math.toRadians(max);
		this.rangeMin = Math.toRadians(min);
	}
}
