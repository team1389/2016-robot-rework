package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.value_types.Position;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Servo;

/**
 * A Servo motor
 * 
 * @author Jacob Prinz
 */
public class ServoHardware extends Hardware<PWM> {
	Servo wpiServo;

	public RangeOut<Position> getPositionOutput() {
		return new RangeOut<Position>((double val) -> {
			wpiServo.set(val);
		}, 0, 1);
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getPositionInput().getWatchable("pos") };
	}

	// TODO check if this max val should be 180?
	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			return wpiServo.getPosition();
		}, 0, 1);
	}

	@Override
	public void initHardware(int port) {
		wpiServo = new Servo(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Servo";
	}
}
