package com.team1389.hardware.inputs.hardware;

import com.kauailabs.navx.frc.AHRS;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.value_types.Position;
import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.SPI;

/**
 * wraps a Kauailabs {@link com.kauailabs.navx.frc.AHRS NavX}, providing stream sources for the yaw, rate, etc.
 * 
 * @author amind
 *
 */
public class NavXHardware implements CompositeWatchable {
	AHRS navX;
	
	/**
	 * @param port the port of the navX
	 * @param registry the registry associated with the robot
	 */
	public NavXHardware(SPI.Port port, Registry registry) {
		navX = new AHRS(port);
		navX.reset();
		registry.registerWatchable(this);
	}

	/**
	 * @return a stream of yaw data from the NavX (in degrees)
	 */
	public AngleIn<Position> getYawInput() {
		return new AngleIn<Position>(Position.class, () -> {
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
		return stem.put(getYawInput().getWatchable("yaw"));
	}
}
