package com.team1389.hardware.outputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Victor;

/**
 * A victor motor controller
 * 
 * @author Jacob Prinz
 */
public class VictorHardware implements Watchable {

	Victor wpiVictor;

	public VictorHardware(int pwmPort, Registry registry) {
		registry.claimPWMPort(pwmPort);
		registry.registerWatcher(this);
		wpiVictor = new Victor(pwmPort);
	}

	public PercentRangeOutput getVoltageOutput() {
		return (double voltage) -> {
			wpiVictor.set(voltage);
		};
	}

	public void invert(boolean inverted) {
		wpiVictor.setInverted(inverted);
	}

	@Override
	public String getName() {
		return "Victor " + wpiVictor.getChannel();
	}

	@Override
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();

		info.put("last set output", "" + wpiVictor.getSpeed());

		return null;
	}
}
