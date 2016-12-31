package com.team1389.system.drive;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.AddList;
import com.team1389.util.DriveSignal;
import com.team1389.watch.Watchable;

/**
 * hellow
 * 
 * @author amind
 *
 */
public class CheesyDriveSystem extends Subsystem {
	private DriveOut<Percent> drive;
	private PercentIn throttle;
	private PercentIn wheel;
	private DigitalIn quickTurnButton;

	private DriveSignal mSignal = DriveSignal.NEUTRAL;

	private double mQuickStopAccumulator;
	private double kTurnSensitivity;

	public CheesyDriveSystem(DriveOut<Percent> drive, PercentIn throttle, PercentIn wheel, DigitalIn quickTurnButton,
			double turnSensitivity) {
		this.drive = drive;
		this.throttle = throttle;
		this.wheel = wheel;
		this.quickTurnButton = quickTurnButton;
		this.kTurnSensitivity = turnSensitivity;
	}

	public CheesyDriveSystem(DriveOut<Percent> drive, PercentIn throttle, PercentIn wheel, DigitalIn quickTurnButton) {
		this(drive, throttle, wheel, quickTurnButton, 1.0);
	}

	@Override
	public void init() {
		throttle.addChangeListener(COMMAND_CANCEL);
		wheel.addChangeListener(COMMAND_CANCEL);
	}

	@Override
	public void update() {
		mSignal = cheesyDrive(throttle.get(), wheel.get(), quickTurnButton.get());
		drive.set(mSignal);
	}

	@Override
	public String getName() {
		return "Drive System";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(drive, quickTurnButton.getWatchable("quickTurnButton"));
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

}
