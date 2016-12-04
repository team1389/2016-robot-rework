package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Angle;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroHardware implements Watchable{
	private Gyro wpiGyro;
	private double port;
	public GyroHardware(int port) {
		this.port=port;
		wpiGyro = new AnalogGyro(port);
		wpiGyro.calibrate();
	}

	public RangeIn<Angle> getAngleInput(){
		return new RangeIn<Angle>(Angle.class,()->{
			return wpiGyro.getAngle();},0d,360d);
	}

	@Override
	public String getName() {
		return "Analog gyro "+port;
	}

	@Override
	public Info[] getInfo() {
		return new Info[]{
				new NumberInfo("gyro angle",()->{
					return wpiGyro.getAngle();
				})
		};
	}
}
