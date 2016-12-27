package com.team1389.drive;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.util.DriveSignal;
import com.team1389.util.RangeUtil;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

/**
 * 
 * @author Raffi
 *
 */
public class FieldOriented implements CompositeWatchable {
	DriveOut<Percent> drive;
	private AngleIn gyro;
	private DriveSignal signal = DriveSignal.NEUTRAL;
	private PercentIn throttleX;
	private PercentIn throttleY;
	private DigitalIn override;
	private DigitalIn nonGyro;

	/**
	 * Constructor to set the variables
	 * 
	 * @param drive dirveOut function -- not entirely sure if needed
	 * @param throttleX x value of joystick
	 * @param throttleY y value of joystick
	 * @param gyro gyroscope
	 * @param override to override some fieldOriented() functions
	 * @param nonGyro to override all fielOriented() functions
	 */
	public FieldOriented(/* DriveOut<Percent> drive, */ PercentIn throttleX, PercentIn throttleY, AngleIn gyro,
			DigitalIn override, DigitalIn nonGyro) {
		this.throttleX = throttleX;
		this.throttleY = throttleY;
		this.gyro = gyro;
		this.override = override;
		this.nonGyro = nonGyro;
		// this.drive = drive; //may not be necessary
	}

	/**
	 * Update method to get new positions
	 */
	public void update() {
		signal = fieldOriented(throttleX.get(), throttleY.get(), gyro.get(), override.get(), nonGyro.get());
		drive.set(signal);
	}

	/**
	 * All the math to get from the variables to a working field oriented drive.
	 * 
	 * @param XThrottle same as above
	 * @param YThrottle ^
	 * @param gyro ^
	 * @param override ^
	 * @param nonGyro ^
	 * @return returns the DriveOut used for field oriented driving
	 */
	public DriveSignal fieldOriented(double XThrottle, double YThrottle, double gyro, boolean override,
			boolean nonGyro) {
		double speedCommand = Math.max(Math.abs(XThrottle), Math.abs(YThrottle));
		double directionCommand = Math.toDegrees(Math.atan2(XThrottle, YThrottle));
		double angleError = directionCommand - gyro;

		angleError = RangeUtil.wrapValue(angleError, -180, 180);

		if (!override) {
			if (angleError > 100) {
				angleError -= 180;
				speedCommand = -speedCommand;
			} else if (angleError < -100) {
				angleError += 180;
				speedCommand = -speedCommand;
			}
		}

		double clockwiseCommand = angleError / 45;

		if (Math.abs(speedCommand) < .1)
			clockwiseCommand = 0;

		if (nonGyro) {
			speedCommand = -XThrottle;
			clockwiseCommand = -YThrottle; // may have to reverse x and y
		}

		double leftSpeed = speedCommand + clockwiseCommand;
		double rightSpeed = speedCommand - clockwiseCommand;

		RangeUtil.limit(leftSpeed, -1, 1);
		RangeUtil.limit(rightSpeed, -1, 1);

		return new DriveSignal(leftSpeed, rightSpeed);

	}

	@Override
	public String getName() {
		return "Field Oriented Drive";
	}

	/**
	 * This is used to display values on the smart dash board Uses lambda expressions, see me or @amind for explanation
	 */
	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { new NumberInfo("Left wheels", () -> {
			return signal.leftMotor;
		}), new NumberInfo("Right wheels", () -> {
			return signal.rightMotor;
		}), gyro.getWatchable("Gyro") };
	}

}
