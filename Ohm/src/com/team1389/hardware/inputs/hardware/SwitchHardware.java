package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.watch.Watchable;

public class SwitchHardware extends Hardware<DIO> {
	boolean inverted;
	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;

	public SwitchHardware() {
		this(false);
	}

	public SwitchHardware(boolean inverted) {
		this.inverted = inverted;
	}


	private boolean get() {
		return inverted ? !wpiSwitch.get() : wpiSwitch.get();
	}

	public DigitalIn getRawSwitch() {
		return new DigitalIn(() -> {
			return get();
		});
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getRawSwitch().getInfo("val") };
	}

	@Override
	public void initHardware(int port) {
		wpiSwitch = new edu.wpi.first.wpilibj.DigitalInput(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Switch";
	}

}
