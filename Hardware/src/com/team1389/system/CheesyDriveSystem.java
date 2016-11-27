package com.team1389.system;

import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.util.DriveSignal;
import com.team1389.hardware.watch.BooleanInfo;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;

public class CheesyDriveSystem extends System {
	private PercentOut leftMotor;
	private PercentOut rightMotor;
	private PercentIn throttle;
	private PercentIn wheel;
	private DigitalInput quickTurnButton;
	private boolean isQuickTurn;
	private double mQuickStopAccumulator;
	private DriveSignal mSignal = new DriveSignal(0, 0);
	private double kTurnSensitivity;

	public CheesyDriveSystem(PercentOut leftMotor, PercentOut rightMotor, PercentIn throttle, PercentIn wheel,
			DigitalInput quickTurnButton, double turnSensitivity) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.throttle = throttle;
		this.wheel = wheel;
		this.quickTurnButton = quickTurnButton;
		this.kTurnSensitivity = turnSensitivity;
	}

	public CheesyDriveSystem(PercentOut leftMotor, PercentOut rightMotor, PercentIn throttle, PercentIn wheel,
			DigitalInput quickTurnButton) {
		this(leftMotor, rightMotor, throttle, wheel, quickTurnButton, 1.0);
	}

	@Override
	public void getInput() {
		isQuickTurn = quickTurnButton.get();
		mSignal = cheesyDrive(throttle.get(), wheel.get(), isQuickTurn);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void defaultUpdate() {
		leftMotor.set(mSignal.leftMotor);
		rightMotor.set(mSignal.rightMotor);
	}

	public void setTurnSensitivity(double val) {
		this.kTurnSensitivity = val;
	}

	public DriveSignal cheesyDrive(double throttle, double wheel, boolean isQuickTurn) {

		double overPower;

		double angularPower;

		if (isQuickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = 0.1;
				mQuickStopAccumulator = (1 - alpha) * mQuickStopAccumulator + alpha * wheel * 2;
			}
			overPower = 1.0;
			angularPower = wheel;
		} else {
			overPower = 0.0;
			angularPower = Math.abs(throttle) * wheel * kTurnSensitivity - mQuickStopAccumulator;
			if (mQuickStopAccumulator > 1) {
				mQuickStopAccumulator -= 1;
			} else if (mQuickStopAccumulator < -1) {
				mQuickStopAccumulator += 1;
			} else {
				mQuickStopAccumulator = 0.0;
			}
		}

		double rightPwm = throttle - angularPower;
		double leftPwm = throttle + angularPower;
		if (leftPwm > 1.0) {
			rightPwm -= overPower * (leftPwm - 1.0);
			leftPwm = 1.0;
		} else if (rightPwm > 1.0) {
			leftPwm -= overPower * (rightPwm - 1.0);
			rightPwm = 1.0;
		} else if (leftPwm < -1.0) {
			rightPwm += overPower * (-1.0 - leftPwm);
			leftPwm = -1.0;
		} else if (rightPwm < -1.0) {
			leftPwm += overPower * (-1.0 - rightPwm);
			rightPwm = -1.0;
		}
		mSignal.rightMotor = rightPwm;
		mSignal.leftMotor = leftPwm;
		return mSignal;
	}

	@Override
	public String getName() {
		return "Drive System";
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo("leftWheels", () -> {
			return mSignal.leftMotor;
		}), new NumberInfo("rightWheels", () -> {
			return mSignal.rightMotor;
		}), new BooleanInfo("isQuickTurn", () -> {
			return isQuickTurn;
		}) };
	}

}
