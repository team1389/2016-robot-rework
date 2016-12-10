package com.team1389.hardware.outputs.hardware;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.watch.Info;

import edu.wpi.first.wpilibj.Victor;

/**
 * A victor motor controller
 * 
 * @author Jacob Prinz
 */
public class VictorHardware extends Hardware<PWM> {

	Victor wpiVictor;

	public PercentOut getVoltageOutput() {
		return new PercentOut((double voltage) -> {
			wpiVictor.set(voltage);
		});
	}

	public void invert(boolean inverted) {
		wpiVictor.setInverted(inverted);
	}

	@Override
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("last set output", "" + wpiVictor.getSpeed());
		// TODO method dysfunctional
		return null;
	}

	@Override
	public void initHardware(int port) {
		wpiVictor = new Victor(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor";
	}
}
