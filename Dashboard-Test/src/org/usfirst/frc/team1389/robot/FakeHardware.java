package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.AddList;
import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

public class FakeHardware extends Hardware<PWM> {
	Optional<Fake> fake;

	public FakeHardware(PWM requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(getTimer().getWatchable("time"));
	}

	@Override
	public void init(PWM port) {
		fake = Optional.of(new Fake(port.index()));
	}

	@Override
	protected String getHardwareIdentifier() {
		return "fake";
	}
	
	@Override
	public void failInit() {
		fake = Optional.empty();
	}

	public RangeIn<Value> getTimer() {
		return new RangeIn<Value>(Value.class, fake.ifPresent(0.0, f -> f.getSystemTime()), 0, 1);
	}

}
