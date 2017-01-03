package com.team1389.hardware.inputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.DigitalInput;

public class SwitchHardware extends Hardware<DIO> {
	boolean inverted;

	public SwitchHardware(boolean inverted, DIO requestedPort, Registry registry) {
		super(requestedPort, registry);
		this.inverted = inverted;
	}

	public SwitchHardware(DIO requestedPort, Registry registry) {
		this(false, requestedPort, registry);
	}

	Optional<DigitalInput> wpiSwitch;

	public DigitalIn getSwitchInput() {
		return new DigitalIn(() -> wpiSwitch.map(this::getRawSwitch).orElse(false));
	}

	private boolean getRawSwitch(DigitalInput switchVal) {
		return inverted ^ switchVal.get();
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(getSwitchInput().getWatchable("val"));
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Switch";
	}

	@Override
	public void init(DIO port) {
		wpiSwitch = Optional.of(new DigitalInput(port.index()));
	}

	@Override
	public void failInit() {
		wpiSwitch = Optional.empty();
	}

}
