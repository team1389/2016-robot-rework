package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.valueTypes.Angle;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroHardware {
	private Gyro wpiGyro;

	public GyroHardware(int port) {
		wpiGyro = new AnalogGyro(port);
	}

	public RangeIn<Angle> getAngleInput(){
		return new RangeIn<Angle>(Angle.class,()->{return wpiGyro.getAngle();},0d,360d);
	}
}
