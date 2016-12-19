package com.team1389.drive;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.util.DriveSignal;
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

	public FieldOriented(PercentIn throtleX, PercentIn throtleY, AngleIn gyro)
	{
		this.throttleX = throttleX;
		this.throttleY = throttleY;
		this.gyro = gyro;
	}

	public void update()
	{
		commandSpeed.FieldOriented(throtleX.get(), throtleY.get());
	}

	public DriveSignal fieldOriented(double XThrottle, double YThrottle, double gyro, boolean overide, boolean nonGyro, boolean reZero)
	{
		double speedCommand = Math.max(Math.abs(XThrottle), Math.abs(YThrottle));
		double directionCommand = Math.toDegrees(Math.atan2(XThrottle, YThrottle));
		double angleError = directionCommand - gyro;

		while (angleError > 180)
			angleError -= 360;

		while (angleError < -180)
			angleError += 360;

		if (!overide)
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

		if (reZero)
			gyro = 0;

		double leftSpeed = speedCommand + clockwiseCommand;
		double rightSpeed = speedCommand - clockwiseCommand;

		if (leftSpeed > 1)
			leftSpeed = 1;
		if (leftSpeed < -1)
			leftSpeed = -1;

		if (rightSpeed > 1)
			rightSpeed = 1;
		if (rightSpeed < -1)
			rightSpeed = -1;

		return commandSpeed;

	}

	@Override
	public String getName()
	{
		return "Field Oriented Drive";
	}

	@Override
	public Watchable[] getSubWatchables()
	{
		return new Watchable[] {new NumberInfo("left wheels", () -> {
			return commandSpeed.leftMotor;}),new NumberInfo("right wheels", () -> {return commandSpeed.rightMotor;});
	}
			
	}
}
