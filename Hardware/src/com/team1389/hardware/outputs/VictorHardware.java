package com.team1389.hardware.outputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.registry.Constructor;
import com.team1389.hardware.registry.PWMPort;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Victor;

/**
 * A victor motor controller
 * 
 * @author Jacob Prinz
 */
public class VictorHardware implements Watchable {
	public static final Constructor<PWMPort, VictorHardware> constructor = (PWMPort port) -> {
		return new VictorHardware(port);
	};

	Victor wpiVictor;

	private VictorHardware(PWMPort port) {
		wpiVictor = new Victor(port.number);
	}

	public PercentRangeOutput getVoltageOutput() {
		return (double voltage) -> {
			wpiVictor.set(voltage);
		};
	}

	@Override
	public String getName() {
		return "Victor " + wpiVictor.getChannel();
	}

	@Override
	public Map<String, String> getInfo() {
		Map<String, String> info = new HashMap<>();

		info.put("last set output", "" + wpiVictor.getSpeed());

		return info;
	}
}
