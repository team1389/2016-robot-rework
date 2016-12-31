package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.util.AddList;
import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Victor;

public class Victor888Hardware extends Hardware<PWM> {

	public Victor888Hardware(boolean inverted, PWM requestedPort, Registry registry) {
		super(requestedPort, registry);
		this.inverted = inverted;
	}

	boolean inverted;
	Optional<Victor> wpiVictor;

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(getVoltageOutput().getWatchable("voltage"));
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(wpiVictor.ifPresent((s, pos) -> s.set(pos)));
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor888";
	}

	@Override
	public void init(PWM port) {
		Victor victor = new Victor(port.index());
		victor.setInverted(inverted);
		wpiVictor = Optional.of(victor);
	}

	@Override
	public void failInit() {
		wpiVictor = Optional.empty();
	}

}
