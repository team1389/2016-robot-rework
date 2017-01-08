package simulation.motor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.util.RangeUtil;

public class MotorSystem extends RotationSim {
	Set<Motor> motors;
	private double gearReduction;
	private Attachment attachment;
	private double rangeMin, rangeMax;

	public MotorSystem(Set<Motor> motors, Attachment attachment, double gearing) {
		this.gearReduction = gearing;
		this.attachment = attachment;
		this.motors = motors;
		this.rangeMax = Double.MAX_VALUE;
		this.rangeMin = Double.MIN_VALUE;
	}

	public MotorSystem(Attachment attachment, double gearing, Motor... motors) {
		this(new HashSet<Motor>(Arrays.asList(motors)), attachment, gearing);
	}

	public MotorSystem(Motor motor, Attachment attachment, double gearing) {
		this(new HashSet<>(), attachment, gearing);
		motors.add(motor);
	}

	public MotorSystem(Motor motor) {
		this(motor, new Attachment(Attachment.FREE, false), 1);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setVoltage);
	}

	public void setRangeOfMotion(double min, double max) {
		this.rangeMax = Math.toRadians(max);
		this.rangeMin = Math.toRadians(min);
	}

	public void setVoltage(double voltage) {
		double limVoltage = RangeUtil.limit(voltage, -1, 1);
		motors.forEach(m -> m.setPercentVoltage(limVoltage));
	}

	@Override
	public void update() {
		super.update();
		if (!((omega > 0 && theta <= rangeMax) || (omega < 0 && theta >= rangeMin))) {
			theta = RangeUtil.limit(theta, rangeMin, rangeMax);
			if (((alpha > 0 && theta > rangeMax) || (alpha < 0 && theta < rangeMin))) {
				omega = 0;
			}
		}
	}

	@Override
	protected double getNetTorque() {
		return getMotorTorque() * gearReduction + getAttachmentTorque();
	}

	@Override
	protected double getMoment() {
		return attachment.getMoment();
	}

	private double getMotorTorque() {
		return motors.stream().mapToDouble(m -> m.getTorque(omega * gearReduction)).sum();
	}

	private double getAttachmentTorque() {
		return attachment.getAddedTorque(theta);
	}
}
