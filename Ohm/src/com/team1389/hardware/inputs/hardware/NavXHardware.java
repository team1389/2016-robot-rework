package com.team1389.hardware.inputs.hardware;

import com.kauailabs.navx.frc.AHRS;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.SPI;

public class NavXHardware implements CompositeWatchable {
	AHRS navX;

	public NavXHardware(SPI.Port port, Registry registry) {
		navX = new AHRS(port);
		navX.reset();
		registry.registerWatchable(this);
	}

	public AngleIn getAngleInput() {
		return new AngleIn(() -> {
			return (double) navX.getYaw();
		});
	}

	@Override
	public String getName() {
		return "NavX";
	}

	// TODO add NavX capabilities
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(getAngleInput().getWatchable("yaw"));
	}
}
