package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.util.OptionalUtil;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Solenoid;

public class FlashlightHardware extends Hardware<PCM> {
	public FlashlightHardware(PCM requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	private Optional<Solenoid> wpiSolenoid;

	public DigitalOut getDigitalOut() {
		return new DigitalOut(OptionalUtil.ifPresent(wpiSolenoid, (Solenoid s, Boolean pos) -> {
			s.set(pos);
		}));
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getDigitalOut().getWatchable("output") };
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Light";
	}

	@Override
	public void init(PCM port) {
		this.wpiSolenoid = Optional.of(new Solenoid(port.index()));
	}
}
