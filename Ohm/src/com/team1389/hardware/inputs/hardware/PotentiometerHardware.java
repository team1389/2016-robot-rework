package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.value_types.Position;
import com.team1389.watch.Info;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class PotentiometerHardware extends Hardware<Analog> {
	private AnalogPotentiometer wpiPot;

	public RangeIn<Position> getAnalogInput() {
		return new RangeIn<>(Position.class, () -> {
			return wpiPot.get();
		}, 0, 1);
	}

	@Override
	public Info[] getInfo() {
		return null;
	}

	@Override
	public void initHardware(int port) {
		this.wpiPot = new AnalogPotentiometer(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Potentiometer";
	}
}
