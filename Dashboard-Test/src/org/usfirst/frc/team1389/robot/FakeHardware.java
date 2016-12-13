package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.watch.Watchable;

public class FakeHardware extends Hardware<PWM>{

	@Override
	public Watchable[] getSubWatchables() {
		return null;
	}

	@Override
	public void initHardware(int port) {
		System.out.println(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "fake";
	}

}
