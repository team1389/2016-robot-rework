package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.watch.BooleanInfo;
import com.team1389.watch.Info;

public class SwitchHardware extends Hardware<DIO> {
	boolean inverted;

	public SwitchHardware() {
		this(false);
	}

	public SwitchHardware(boolean inverted) {
		this.inverted = inverted;
	}

	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;

	private boolean get() {
		return inverted ? !wpiSwitch.get() : wpiSwitch.get();
	}

	public BinaryInput getRawSwitch() {
		return () -> {
			return get();
		};
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new BooleanInfo(getName(), () -> {
			return wpiSwitch.get();
		}) };
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
