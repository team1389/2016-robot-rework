package com.team1389.hardware.outputs.hardware;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Servo;

/**
 * A Servo motor
 * 
 * @author Jacob Prinz
 */
public class ServoHardware implements Watchable {
	Servo wpiServo;

	/**
	 * @param port
	 *            PWM port that servo is plugged into
	 */
	public ServoHardware(int pwmPort, Registry registry) {
		registry.claimPWMPort(pwmPort);
		registry.registerWatcher(this);

		wpiServo = new Servo(pwmPort);
	}

	public int getPort() {
		return wpiServo.getChannel();
	}

	public RangeOut<Position> getPositionOutput() {
		return new RangeOut<Position>((double val) -> {
			wpiServo.set(val);
		} , 0, 1);
	}

	@Override
	public String getName() {
		return "Servo " + getPort();
	}

	@Override
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("position", "" + wpiServo.getPosition());
		return null;
	}

	// TODO check if this max val should be 180?
	public RangeIn getPositionInput() {
		return new RangeIn(() -> {
			return wpiServo.getPosition();
		} , 0, 1);
	}
}
