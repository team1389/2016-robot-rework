package com.team1389.hardware.inputs.hardware;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.AddList;
import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SensorBase;

public class PDPHardware extends Hardware<CAN> {
	static PDPHardware instance;
	Optional<PowerDistributionPanel> wpiPDP;

	public static PDPHardware getInstance(Registry registry) {
		return instance;
	}

	public PDPHardware(Registry registry) {
		super(new CAN(0), registry);
	}

	public RangeIn<Value> getCurrentIn(int port) {
		get(PDPHardware::new);
		return new RangeIn<Value>(Value.class, wpiPDP.ifPresent(0.0, pdp -> pdp.getTotalCurrent()), 0, 1);
	}

	public void get(Consumer<Registry> hardware) {

	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		stem.addAll(IntStream.range(0, SensorBase.kPDPChannels)
				.mapToObj(port -> getCurrent(port).getWatchable("port " + port + " current"))
				.collect(Collectors.toList()));
		return stem;
	}

	public RangeIn<Value> getCurrent(int port) {
		return new RangeIn<Value>(Value.class, wpiPDP.ifPresent(0.0, pdp -> pdp.getCurrent(port)), 0, 100);
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
