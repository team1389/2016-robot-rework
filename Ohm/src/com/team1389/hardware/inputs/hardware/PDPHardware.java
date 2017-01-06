package com.team1389.hardware.inputs.hardware;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * represents a Power Distribution Panel attached to the robot's CAN interface can track the current being drawn from ports on the PDP
 * 
 * @author amind
 *
 */
public class PDPHardware extends Hardware<CAN> {
	Optional<PowerDistributionPanel> wpiPDP;

	/**
	 * @param registry the registry associated with the robot generates an instance of the PDP on the default CAN port (0)
	 * @see <a href="http://wpilib.screenstepslive.com/s/4485/m/13809/l/219414-power-distribution-panel">WPILib PDP docs</a>
	 */
	public PDPHardware(Registry registry) {
		super(new CAN(0), registry);
	}

	/**
	 * @param port port to check current on
	 * @return a value stream that tracks the current flowing through the port
	 */
	public RangeIn<Value> getCurrentIn(int port) {
		return new RangeIn<Value>(Value.class, () -> wpiPDP.map(pdp -> pdp.getCurrent(port)).orElse(0.0), 0, 1);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		stem.addAll(IntStream.range(0, SensorBase.kPDPChannels)
				.mapToObj(port -> getCurrentIn(port).getWatchable("port " + port + " current"))
				.collect(Collectors.toList()));
		return stem;
	}

	/**
	 * 
	 * @return
	 * TODO determine max PDP current
	 */
	public RangeIn<Value> getCurrentIn() {
		return new RangeIn<Value>(Value.class, () -> wpiPDP.map(pdp -> pdp.getTotalCurrent()).orElse(0.0), 0, 100);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "PDP";
	}

	@Override
	public void init(CAN port) {
	}

	@Override
	public void failInit() {
	}
}
