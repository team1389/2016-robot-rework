package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class SimMotor {
	Motor motor;
	Attachment attachment;
	public final static double g = 9.8; // acceleration of gravity (m/s^2)
	private double gearReduction;

	private double theta = 0; // current angle
	private double omega = 0; // current rate of rotation (rad/sec)
	private double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private double currentTorque; // torque from motor
	private Timer timer;

	public SimMotor(Motor motor, Attachment attachment, double gearing) {
		timer = new Timer();
		this.gearReduction = gearing;
		this.attachment=attachment;
		this.motor = motor;
	}

	public SimMotor(Motor motor) {
		this(motor, Attachment.FREE, 1);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setVoltage);
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> theta / gearReduction, 0, 2 * Math.PI);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> omega / gearReduction, 0, 2 * Math.PI);
	}

	public void setVoltage(double voltage) {
		double dt = timer.get();
		timer.zero();
		currentTorque = motor.getTorque(voltage, omega) * gearReduction; // calculate torque from motor, which depends on the voltage and the current speed
		alpha = attachment.getResultantAlpha(currentTorque, theta);
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
	}

	public static class Motor {
		public static final Motor CIM = new Motor(2.41, 5330);
		public static final Motor m775_PRO = new Motor(.71, 18730);
		public static final Motor MINI_CIM = new Motor(1.4, 5840);
		public static final Motor BAG_MOTOR = new Motor(.4, 13180);

		public final double stallTorque;
		public final double freeSpeed;
		public final double motorConstant;

		public Motor(double stallTorque, double freeSpeedRPM) {

			this.stallTorque = stallTorque;
			this.freeSpeed = freeSpeedRPM * 2 * Math.PI / 60;
			this.motorConstant = -freeSpeed / stallTorque;
		}

		/**
		 * Calculate the torque contributed from the CIM motor.
		 * 
		 * @param voltage
		 * @param omega
		 * @return torque from motor
		 */
		public double getTorque(double voltage, double omega) {
			return stallTorque * (voltage - (omega / freeSpeed));
		}
	}

	public static class Attachment {
		static final Attachment FREE = new Attachment(1, .05, .008, 1.6, false);

		public final double I, rCenterMass, m; // moment of inertia
		private final boolean hasWeight;

		public Attachment(double m, double r, double rCenterMass, double I, boolean hasWeight) {
			this.m = m;
			this.rCenterMass = rCenterMass;
			this.I = I;
			this.hasWeight = hasWeight;
		}

		public Attachment(double m, double r, double rCenterMass, boolean hasWeight) {
			this(m, r, rCenterMass, m * rCenterMass * rCenterMass + m * r * r / 12, // calculate moment of inertia of the arm
					hasWeight);
		}

		public Attachment(double m, double r, boolean hasWeight) {
			this(m, r, r / 2, hasWeight);
		}

		public double getResultantAlpha(double torqueApplied, double theta) {
			if (hasWeight) {
				double torqueGravity = g * m * Math.cos(theta) * rCenterMass;
				return (torqueApplied - torqueGravity) / I;
			} else {
				return torqueApplied / I;
			}
		}

	}
}
