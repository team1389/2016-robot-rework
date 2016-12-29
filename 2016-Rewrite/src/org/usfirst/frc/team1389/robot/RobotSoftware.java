package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.DriveOut;

public class RobotSoftware extends RobotHardware {
	private static RobotSoftware INSTANCE = new RobotSoftware();

	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

	public AngleIn turretAngle = turretGyro.getAngleInput().sumInputs(navX.getAngleInput()).setRange(-180, 180).getWrapped();
	
	public DigitalIn IRsensors = IRsensor1.getRawSwitch().combineOR(IRsensor2.getRawSwitch());
	
	public DriveOut<Percent> drive = new DriveOut<Percent>(leftDrive.getVoltageOutput(), rightDrive.getVoltageOutput());
	
	public AngleIn armVal = armPot.getAnalogInput().mapToRange(120, 0).setRange(0, 76).mapToRange(0, 90).setRange(0, 360).mapToAngle();

}