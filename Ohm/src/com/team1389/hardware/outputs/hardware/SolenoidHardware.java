package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Solenoid;

public class SolenoidHardware extends Hardware<PCM> {
	public SolenoidHardware(PCM requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	private Optional<Solenoid> wpiSolenoid;

	public DigitalOut getDigitalOut() {
		return new DigitalOut(val -> wpiSolenoid.ifPresent(w -> w.set(val)));
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(getDigitalOut().getWatchable("state"));
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Solenoid";
	}

	@Override
	public void init(PCM port) {
		wpiSolenoid = Optional.of(new Solenoid(port.index()));
	}

	@Override
	public void failInit() {
		wpiSolenoid = Optional.empty();
	}

}
