package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.interfaces.DigitalOut;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.watch.info.Info;

import edu.wpi.first.wpilibj.Solenoid;

public class FlashlightHardware extends Hardware<PCM> {
	private Solenoid wpiSolenoid;

	public DigitalOut getDigitalOut() {
		return (boolean val) -> {
			wpiSolenoid.set(val);
		};
	}

	@Override
	public Info[] getInfo() {
		return null;
	}

	@Override
	public void initHardware(int port) {
		this.wpiSolenoid = new Solenoid(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Light";
	}
}
