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
public class FieldOriented implements CompositeWatchable
{
	DriveOut<Percent> drive;
	private AngleIn gyro;
	private DriveSignal commandSpeed = new DriveSignal(0, 0);
	private PercentIn throttleX;
	private PercentIn throttleY;
	private DigitalIn override;
	private DigitalIn nonGyro;

	public FieldOriented(DriveOut<Percent> drive, PercentIn throttleX, PercentIn throttleY, AngleIn gyro, DigitalIn override, DigitalIn nonGyro)
	{
		this.throttleX = throttleX;
		this.throttleY = throttleY;
		this.gyro = gyro;
		this.override = override;
		this.nonGyro = nonGyro;
		this.drive = drive;
	}

	public void update()
	{
		DriveSignal signal = fieldOriented(throttleX.get(), throttleY.get(), gyro.get(), override.get(), nonGyro.get());
		drive.set(signal);
	}

	public DriveSignal fieldOriented(double XThrottle, double YThrottle, double gyro, boolean override, boolean nonGyro)
	{
		double speedCommand = Math.max(Math.abs(XThrottle), Math.abs(YThrottle));
		double directionCommand = Math.toDegrees(Math.atan2(XThrottle, YThrottle));
		double angleError = directionCommand - gyro;

		angleError = RangeUtil.wrapValue(angleError, -180, 180);

		if (!override)
		{
			if (angleError > 100)
			{
				angleError -= 180;
				speedCommand = -speedCommand;
			}
			else if (angleError < -100)
			{
				angleError += 180;
				speedCommand = -speedCommand;
			}
		}

		double clockwiseCommand = angleError / 45;

		if (Math.abs(speedCommand) < .1)
			clockwiseCommand = 0;

		if (nonGyro)
		{
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
	public String getName()
	{
		return "Field Oriented Drive";
	}

	@Override
	public Watchable[] getSubWatchables()
	{
		return new Watchable[] { new NumberInfo("Left wheels", () -> {
			return commandSpeed.leftMotor;
		}), new NumberInfo("Right wheels", () -> {
			return commandSpeed.rightMotor;
		}), gyro.getWatchable("Gyro") };
	}

}
