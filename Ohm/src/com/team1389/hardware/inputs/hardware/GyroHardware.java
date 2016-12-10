package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.value_types.Angle;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroHardware extends Hardware<Analog> {
	private Gyro wpiGyro;

	public RangeIn<Angle> getAngleInput() {
		return new RangeIn<Angle>(Angle.class, () -> {
			return wpiGyro.getAngle();
		}, 0d, 360d);
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo("gyro angle", () -> {
			return wpiGyro.getAngle();
		}) };
	}

	@Override
	public void initHardware(int port) {
		wpiGyro = new AnalogGyro(port);
		wpiGyro.calibrate();
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Gyro";
	}
}
