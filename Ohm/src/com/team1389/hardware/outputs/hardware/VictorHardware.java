package com.team1389.hardware.outputs.hardware;


import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.util.Optional;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.FlagInfo;

import edu.wpi.first.wpilibj.VictorSP;

/**
 * A victor motor controller
 * 
 * @author Jacob Prinz
 */
public class VictorHardware extends Hardware<PWM> {

	Optional<VictorSP> wpiVictor;
	boolean inverted;

	public VictorHardware(boolean inverted, PWM port, Registry registry) {
		super(port, registry);
		this.inverted = inverted;
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(wpiVictor.ifPresent((VictorSP s, Double pos) -> {
			s.set(pos);
		}));
	}

	// TODO remove the port fault flag from here once its implemented in hardware
	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getVoltageOutput().getWatchable("voltage"),
				new FlagInfo("port fault", port::isPresent) };
	}

	@Override
	public void init(PWM port) {
		VictorSP myVictor = new VictorSP(port.index());
		myVictor.setInverted(inverted);
		wpiVictor = Optional.of(myVictor);
	}
	@Override
	public void failInit() {
		wpiVictor = Optional.empty();
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor";
	}

}
