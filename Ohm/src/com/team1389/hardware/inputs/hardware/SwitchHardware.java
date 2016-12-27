package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.util.Optional;
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

	public DigitalIn getRawSwitch() {
		return new DigitalIn(wpiSwitch.ifPresent(false, (DigitalInput inp) -> {
			return inverted ? !inp.get() : inp.get();
		}));
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getRawSwitch().getInfo("val") };
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
