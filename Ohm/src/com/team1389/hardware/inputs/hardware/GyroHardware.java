package com.team1389.hardware.inputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * wraps a WPILib {@link edu.wpi.first.wpilibj.AnalogGyro AnalogGyro}, providing stream sources for the gyro position and rate
 * 
 * @author amind
 *
 */
public class GyroHardware extends Hardware<Analog> {
	/**
	 * @param requestedPort the port to attempt to initialize this hardware
	 * @param registry the registry associated with the robot
	 */
	public GyroHardware(Analog requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	private Optional<Gyro> wpiGyro;

	/**
	 * @return a stream of the gyro position (in degrees)
	 */
	public AngleIn<Position> getAngleInput() {
		return new AngleIn<Position>(Position.class, () -> wpiGyro.map(gyr -> gyr.getAngle()).orElse(0.0));
	}

	/**
	 * @return a stream of the gyro rate (in degrees)
	 */
	public AngleIn<Speed> getRateInput() {
		return new AngleIn<Speed>(Speed.class, () -> wpiGyro.map(gyr -> gyr.getRate()).orElse(0.0));
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(getAngleInput().getWatchable("angle"));
	}

	@Override
	public void init(Analog port) {
		Gyro gyr = new AnalogGyro(port.index());
		gyr.calibrate();
		wpiGyro = Optional.of(gyr);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Gyro";
	}

	@Override
	public void failInit() {
		wpiGyro = Optional.empty();
	}
}
