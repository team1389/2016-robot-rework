package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Solenoid;

public class FlashlightHardware extends Hardware<PCM> {
	private Solenoid wpiSolenoid;

	public DigitalOut getDigitalOut() {
		return new DigitalOut((boolean val) -> {
			wpiSolenoid.set(val);
		});
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getDigitalOut().getWatchable("output") };
	}

	@Override
	public void init(int port) {
		this.wpiSolenoid = new Solenoid(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Light";
	}
}
