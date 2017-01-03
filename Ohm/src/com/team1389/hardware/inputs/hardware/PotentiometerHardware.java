package com.team1389.hardware.inputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.value_types.Position;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class PotentiometerHardware extends Hardware<Analog> {
	public PotentiometerHardware(Analog requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	private Optional<AnalogPotentiometer> wpiPot;

	public RangeIn<Position> getAnalogInput() {
		return new RangeIn<>(Position.class, () -> wpiPot.map(pot -> pot.get()).orElse(0.0), 0, 1);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(getAnalogInput().getWatchable("val"));
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Potentiometer";
	}

	@Override
	public void init(Analog port) {
		wpiPot = Optional.of(new AnalogPotentiometer(port.index()));
	}

	@Override
	public void failInit() {
		wpiPot = Optional.empty();
	}
}
