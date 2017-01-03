package motor_sim;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.AddList;
import com.team1389.util.RangeUtil;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

public class SimulatedActuator implements CompositeWatchable {
	Set<Motor> motors;
	private double gearReduction;
	private Attachment attachment;
	private double rangeMin, rangeMax;

	private double theta = 0; // current angle
	private double omega = 0; // current rate of rotation (rad/sec)
	private double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private Timer timer;

	public SimulatedActuator(Set<Motor> motors, Attachment attachment, double gearing) {
		timer = new Timer();
		this.gearReduction = gearing;
		this.attachment = attachment;
		this.motors = motors;
		this.rangeMax = Double.MAX_VALUE;
		this.rangeMin = Double.MIN_VALUE;
	}

	public SimulatedActuator(Attachment attachment, double gearing, Motor... motors) {
		this(new HashSet<Motor>(Arrays.asList(motors)), attachment, gearing);
	}

	public SimulatedActuator(Motor motor, Attachment attachment, double gearing) {
		this(new HashSet<>(), attachment, gearing);
		motors.add(motor);
	}

	public SimulatedActuator(Motor motor) {
		this(motor, new Attachment(Attachment.FREE, false), 1);
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
		motors.forEach(m -> m.setPercentVoltage(limVoltage));
	}

	public void update() {
		double dt = timer.get();
		alpha = calculateAlpha();
		omega += alpha * dt; // add to velocity
		if ((omega > 0 && getPosition() <= rangeMax) || (omega < 0 && getPosition() >= rangeMin)) {
			theta += omega * dt; // add to position
		} else {
			theta = RangeUtil.limit(theta, rangeMin, rangeMax);
			omega = 0;
		}
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
		double totalTorque = getMotorTorque() * gearReduction + getAttachmentTorque();
		return totalTorque / attachment.getMoment();
	}

	private double getMotorTorque() {
		return motors.stream().mapToDouble(m -> m.getTorque(getOmega() / gearReduction)).sum();
	}

	private double getAttachmentTorque() {
		return attachment.getAddedTorque(getPosition());
	}

	/**
	 * limits the actuator to the given range of motion
	 * 
	 * @param min the minimum angle of the actuator (degrees)
	 * @param max the maximum angle of the actuator (degrees)
	 */
	public void setRangeOfMotion(double min, double max) {
		this.rangeMax = Math.toRadians(max);
		this.rangeMin = Math.toRadians(min);
	}

	@Override
	public String getName() {
		return "Simulated Actuator";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(new NumberInfo("attachment torque", this::getAttachmentTorque),
				new NumberInfo("motor torque", this::getMotorTorque),
				new NumberInfo("position", this::getPositionDegrees), new NumberInfo("speed", this::getOmegaDegrees),
				new NumberInfo("accel", this::calculateAlpha));
	}
}