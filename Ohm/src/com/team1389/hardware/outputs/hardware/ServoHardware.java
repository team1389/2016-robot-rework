package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.AngleOut;
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
		return new Watchable[] { getAngleInput().getWatchable("angle") };
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			return wpiServo.getPosition();
		}, 0, 1);
	}

	public AngleIn getAngleInput() {
		return new AngleIn(() -> {
			return wpiServo.getAngle();
		});
	}

	public AngleOut getAngleOutput() {
		return new AngleOut((double val) -> {
			wpiServo.setAngle(val);
		});
	}

	@Override
	public void init(int port) {
		wpiServo = new Servo(port);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Servo";
	}
}
