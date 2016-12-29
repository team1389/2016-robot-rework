package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class SimMotor {
	Timer timer;
	double g = 9.8; // acceleration of gravity (m/s^2)
	public double I = .06; // moment of inertia
	double rad_per_sec_per_nm = -237.7; // motor constant
	double free_speed = 575.9586525; // motor contstant (CIM, in rad/sec)
	double stall_torque = -free_speed / rad_per_sec_per_nm; // all motor constants should be derived from the two given
	double tau_m; // torque from motor
	double m;

	public double theta = 0; // current angle
	public double omega = 0; // current rate of rotation (rad/sec)
	public double alpha = 0; // current acceleration of rotation (rad^2/sec)

	public SimMotor(double m) {
		this.m = m;
		timer = new Timer();
	}

	public SimMotor() {
		this(.001);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(this::setArmVoltage);
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> theta, 0, 2 * Math.PI);
	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> omega, 0, 2 * Math.PI);
	}

	/**
	 * A simple arm with no gravity acting on it.
	 * 
	 * @param voltage
	 * @return angle of the CIM motor shaft (not arm!)
	 */
	public double setVoltage(double voltage) {
		double dt = timer.get();
		timer.zero();
		tau_m = motorTorque(voltage, omega); // calculate torque from motor, which depends on the voltage and the current speed
		alpha = tau_m / m; // calculate the acceleration of the system. In this case, I = mr^2, and r = 1, so I = m.
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
		return theta;
	}

	// arm with gravity
	public double setArmVoltage(double voltage) {
		double dt = timer.get();
		timer.zero();
		tau_m = motorTorque(voltage, omega); // calculate torque from motor, which depends on the voltage and the current speed
		// calculate the torque of gravity
		double tau_g = g * m * Math.cos(theta / 150);
		alpha = (tau_m - tau_g) / m; // In this case, I = mr^2, and r = 1, so I = m.
		omega += alpha * dt; // add to velocity
		theta += omega * dt; // add to position
		return theta;
	}

	/**
	 * Calculate the torque contributed from the CIM motor.
	 * 
	 * @param voltage
	 * @param omega
	 * @return torque from motor
	 */
	public double motorTorque(double voltage, double omega) {
		return stall_torque * (voltage - (omega / free_speed));
	}
}
