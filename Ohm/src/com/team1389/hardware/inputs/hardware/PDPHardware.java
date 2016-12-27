package com.team1389.hardware.inputs.hardware;

import java.util.Arrays;
import java.util.function.Consumer;
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
	static PDPHardware instance;
	PowerDistributionPanel wpiPDP;

	public static PDPHardware getInstance(Registry registry) {
		return instance;
	}

	public PDPHardware(Registry registry) {
		super(new CAN(0), registry);
	}

	public RangeIn<Value> getCurrentIn(int port) {
		get(PDPHardware::new);
		return new RangeIn<Value>(Value.class, () -> {
			return wpiPDP.getCurrent(port);
		}, 0, 1);
	}
	
	public void get(Consumer<Registry> hardware){
		
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
	protected String getHardwareIdentifier() {
		return "PDP";
	}

	@Override
	public void init(CAN port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failInit() {
		// TODO Auto-generated method stub

	}
}
