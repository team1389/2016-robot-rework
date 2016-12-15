package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Victor;

/**
 * A victor motor controller
 * 
 * @author Jacob Prinz
 */
public class VictorHardware extends Hardware<PWM> {

	Victor wpiVictor;
	boolean inverted;

	public VictorHardware(boolean inverted) {
		this.inverted = inverted;
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut((double voltage) -> {
			wpiVictor.set(voltage);
		});
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getVoltageOutput().getWatchable("voltage") };
	}

	@Override
	public void init(int port) {
		wpiVictor = new Victor(port);
		wpiVictor.setInverted(inverted);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor";
	}
}
