package com.team1389.drive;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.util.DriveSignal;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

public class CheesyDrive implements CompositeWatchable {
	DriveOut<Percent> drive;
	private PercentIn throttle;
	private PercentIn wheel;
	private DigitalIn quickTurnButton;
	private double mQuickStopAccumulator;
	private DriveSignal mSignal = new DriveSignal(0, 0);
	private double kTurnSensitivity;

	public CheesyDrive(DriveOut<Percent> drive, PercentIn throttle, PercentIn wheel, DigitalIn quickTurnButton,
			double turnSensitivity) {
		this.drive = drive;
		this.throttle = throttle;
		this.wheel = wheel;
		this.quickTurnButton = quickTurnButton;
		this.kTurnSensitivity = turnSensitivity;
	}

	public CheesyDrive(DriveOut<Percent> drive, PercentIn throttle, PercentIn wheel, DigitalIn quickTurnButton) {
		this(drive, throttle, wheel, quickTurnButton, 1.0);
	}

	public void update() {
		mSignal = cheesyDrive(throttle.get(), wheel.get(), quickTurnButton.get());
		drive.set(mSignal);
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
		return "Cheesy Drive";
	}

	// TODO think about watchable implementation for drive classes
	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { new NumberInfo("leftWheels", () -> {
			return mSignal.leftMotor;
		}), new NumberInfo("rightWheels", () -> {
			return mSignal.rightMotor;
		}), quickTurnButton.getInfo("quickTurnButton") };
	}
}
