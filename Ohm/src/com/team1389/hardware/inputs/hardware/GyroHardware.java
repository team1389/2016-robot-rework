package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroHardware extends Hardware<Analog> {
	private Gyro wpiGyro;

	public AngleIn getAngleInput() {
		return new AngleIn(() -> {
			return wpiGyro.getAngle();
		});
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getAngleInput().getWatchable("angle") };
	}

	@Override
	public void init(int port) {
		wpiGyro = new AnalogGyro(port);
		wpiGyro.calibrate();
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Gyro";
	}
}
