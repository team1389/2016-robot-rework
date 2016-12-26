package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BooleanSupplier;
import com.team1389.hardware.inputs.interfaces.POVInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.DigitalIn.InputType;
import com.team1389.hardware.inputs.software.PercentIn;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class JoystickHardware {
	final Joystick wpiJoystick;

	public JoystickHardware(int port) {
		wpiJoystick = new Joystick(port);
	}

	public DigitalIn getButton(int button) {
		return new DigitalIn(getRawButton(button));
	}

	public DigitalIn getButton(int button, InputType type) {
		return new DigitalIn(getRawButton(button), type);
	}

	public BooleanSupplier getRawButton(int button) {
		return () -> {
			return wpiJoystick.getRawButton(button);
		};
	}

	public PercentIn getAxis(int axis) {
		return new PercentIn(() -> {
			return wpiJoystick.getRawAxis(axis);
		});
	}

	public POVInput getPov() {
		return () -> {
			return wpiJoystick.getPOV();
		};
	}

	/**
	 * @param left intensity from 0 to 1
	 * @param right intensity from 0 to 1
	 */
	public void setRumble(double left, double right) {
		setLeftRumble(left);
		setRightRumble(right);
	}

	/**
	 * @param strength intensity of rumble from 0 to 1
	 */
	public void setLeftRumble(double strength) {
		wpiJoystick.setRumble(RumbleType.kLeftRumble, (float) strength);
	}

	/**
	 * @param strength intensity of rumble from 0 to 1
	 */
	public void setRightRumble(double strength) {
		wpiJoystick.setRumble(RumbleType.kRightRumble, (float) strength);
	}
}
