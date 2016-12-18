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
	private DriveSignal speedCommand = new DriveSignal(0, 0);
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
		speedCommand.FieldOriented(throtleX.get(), throtleY.get());
	}

	public DriveSignal fieldOriented(double XThrottle, double YThrottle, double gyro)
	{
		double forward = 0;
		double right = 90;
		double backward = 180;
		double left = 270;
		double directionCommand = Math.atan2(XThrottle, YThrottle) * (180 / Math.PI);
		double angleError = directionCommand - gyro;
		
		
		
		return speedCommand;
	}

	@Override
	public String getName()
	{
		return "Field Oriented Drive";
	}

	@Override
	public Watchable[] getSubWatchables()
	{
		// return gyroValue, throle value, and output
	}
}
