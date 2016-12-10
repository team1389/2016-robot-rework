package com.team1389.hardware.inputs.hardware;

import com.kauailabs.navx.frc.AHRS;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.value_types.Angle;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.SPI;

public class NavXHardware implements Watchable {
	AHRS navX;

	public NavXHardware(SPI.Port port, Registry registry) {
		navX = new AHRS(port);
		navX.reset();
		registry.registerWatchable(this);
	}

	public RangeIn<Angle> getAngleInput() {
		return new RangeIn<Angle>(Angle.class, () -> {
			return navX.getYaw();
		}, 0d, 360d);
	}

	@Override
	public String getName() {
		return "NavX";
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo("navX angle", () -> {
			return navX.getYaw();
		}) };
	}
}
