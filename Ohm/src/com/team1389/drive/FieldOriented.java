package com.team1389.drive;

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
	private PercentIn throttle;
	private PercentIn wheel;
	private DigitalIn quickTurnButton;
	private double mQuickStopAccumulator;
	private DriveSignal speedCommand = new DriveSignal(0, 0);
	private double throtleX;
	private double throtleY;
	
	public FieldOriented(double throtleX, double throtleY)
	{
		this.throtleX = throtleX;
		this.throtleY = throtleY;
	}
	
	public void update(DriveSignal speedCommand, double throtleX, double throtleY)
	{
		speedCommand.FieldOriented(throtleX, throtleY);
	}

	
	@Override
	public String getName()
	{
		return "Field Oriented Drive";
	}
	@Override
	public Watchable[] getSubWatchables()
	{
		/*return new Watchable[] { new NumberInfo("leftWheels", () -> {
			return mSignal.leftMotor;
		}), new NumberInfo("rightWheels", () -> {
			return mSignal.rightMotor;
		}), quickTurnButton.getInfo("quickTurnButton") };*/
	}
}
