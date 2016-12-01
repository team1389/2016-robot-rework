package com.team1389.hardware.inputs.hardware;

import com.kauailabs.navx.frc.AHRS;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Angle;

import edu.wpi.first.wpilibj.SPI;

public class NavXHardware {
	AHRS navX;

	public NavXHardware(SPI.Port port) {
		navX = new AHRS(port);
	}

	public RangeIn<Angle> getAngleInput() {
		return new RangeIn<Angle>(Angle.class, () -> {
			return navX.getAngle();
		}, 0d, 360d);
	}
}
