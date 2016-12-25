package com.team1389.hardware.inputs.hardware;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SensorBase;

public class PDPHardware extends Hardware<CAN> {
	PowerDistributionPanel wpiPDP;

	public static PDPHardware getInstance(Registry registry) {
		return registry.claim(new CAN(0));
	}

	public PDPHardware() {
		Registry.getInstance().claim(new CAN(0));
	}
	public RangeIn<Value> getCurrentIn(int port) {
		return new RangeIn<Value>(Value.class, () -> {
			return wpiPDP.getCurrent(port);
		}, 0, 1);
	}

	@Override
	public Watchable[] getSubWatchables() {
		return Stream
				.concat(IntStream.range(0, SensorBase.kPDPChannels).mapToObj(port -> new NumberInfo(port + "", () -> {
					return wpiPDP.getCurrent(port);
				})), Arrays.stream(new Watchable[] { new NumberInfo("total current", () -> {
					return wpiPDP.getTotalCurrent();
				}) })).toArray(Watchable[]::new);
	}

	@Override
	public void init(int port) {
	}

	@Override
	protected String getHardwareIdentifier() {
		return "PDP";
	}
}
