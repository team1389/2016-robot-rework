package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.util.AddList;
import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroHardware extends Hardware<Analog> {
	public GyroHardware(Analog requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	private Optional<Gyro> wpiGyro;

	public AngleIn getAngleInput() {
		return new AngleIn(wpiGyro.ifPresent(0.0, gyr -> {
			return gyr.getAngle();
		}));
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
