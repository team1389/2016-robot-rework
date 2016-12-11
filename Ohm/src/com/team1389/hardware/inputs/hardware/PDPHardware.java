package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDPHardware extends Hardware<CAN> {
	PowerDistributionPanel wpiPDP;

	public static PDPHardware getInstance(Registry registry) {
		return registry.add(new CAN(0), new PDPHardware());
	}

	public PDPHardware() {
	}

	public RangeIn<Value> getCurrentIn(int port) {
		return new RangeIn<Value>(Value.class, () -> {
			return wpiPDP.getCurrent(port);
		}, 0, 1);
	}

	@Override
	public Watchable[] getSubWatchables() {
		// TODO learn more about streams
		return null;
	}

	@Override
	public void initHardware(int port) {
	}

	@Override
	protected String getHardwareIdentifier() {
		return "PDP";
	}
}
