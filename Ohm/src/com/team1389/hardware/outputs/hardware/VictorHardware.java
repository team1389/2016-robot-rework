package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.watch.Watchable;

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
		return new PercentOut((double val) -> {
			wpiVictor.ifPresent((VictorSP vic) -> {
				vic.set(val);
			});
		});
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getVoltageOutput().getWatchable("voltage") };
	}

	@Override
	public void init(int port) {
		VictorSP myVictor = new VictorSP(port);
		myVictor.setInverted(inverted);
		wpiVictor = Optional.of(myVictor);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor";
	}
}
