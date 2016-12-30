package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class SimMotor {
	Motor motor;

	public final static double g = 9.8; // acceleration of gravity (m/s^2)
	private double I, rCenterMass, m; // moment of inertia
	private boolean hasWeight = true;
	private double gearReduction = 200;

	private double theta = 0; // current angle
	private double omega = 0; // current rate of rotation (rad/sec)
	private double alpha = 0; // current acceleration of rotation (rad^2/sec)
	private double currentTorque; // torque from motor
	private Timer timer;

	public SimMotor(Motor motor, double m, double r, double rCenterMass, double I, boolean hasWeight) {
		timer = new Timer();
		this.motor = motor;
		this.m = m;
		this.rCenterMass = rCenterMass;
		this.I = I;
		this.hasWeight = hasWeight;
	}

	/**
	 * assumes arm is a uniform rod
	 * 
	 * @param motor
	 * @param m
	 * @param r
	 * @param rCenterMass
	 */
	public SimMotor(Motor motor, double m, double r, double rCenterMass, boolean hasWeight) {
		this(motor, m, r, rCenterMass, m * rCenterMass * rCenterMass + m * r * r / 12, // calculate moment of inertia of the arm
				hasWeight);
	}

	public SimMotor(Motor motor, double m, double r, boolean hasWeight) {
		this(motor, m, r, r / 2, m * r * r / 3, hasWeight);
	}

	public SimMotor(Motor motor) {
		this.motor = motor;
		timer = new Timer();
		hasWeight = false;
		I = .001;
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setVoltageGravity);
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> theta / gearReduction, 0, 2 * Math.PI);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> omega / gearReduction, 0, 2 * Math.PI);
	}

	/**
	 * if the weight constants were set, uses gravity, otherwise uses no gravity
	 */
	public void setVoltage(double voltage) {
		if (hasWeight) {
			setVoltageGravity(voltage);
		} else {
			setVoltageNoGravity(voltage);
		}
	}

	/**
	 * A simple arm with no gravity acting on it.
	 * 
	 * @param voltage
	 * @return angle of the CIM motor shaft (not arm!)
	 */
	public void setVoltageNoGravity(double voltage) {
		double dt = timer.get();
		timer.zero();
		currentTorque = motorTorque(voltage, omega) * gearReduction; // calculate torque from motor, which depends on the voltage and the current speed
		alpha = currentTorque / I;
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
	}

	// arm with gravity
	public void setVoltageGravity(double voltage) {
		double dt = timer.get();
		timer.zero();
		currentTorque = motorTorque(voltage, omega)*gearReduction; // calculate torque from motor, which depends on the voltage and the current speed
		// calculate the torque of gravity
		double torqueGravity = g * m * Math.cos(theta) * rCenterMass;
		alpha = (currentTorque - torqueGravity) / I;
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
	}

	/**
	 * Calculate the torque contributed from the CIM motor.
	 * 
	 * @param voltage
	 * @param omega
	 * @return torque from motor
	 */
	public double motorTorque(double voltage, double omega) {
		return motor.stallTorque * (voltage - (omega / motor.freeSpeed));
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
	}
}
