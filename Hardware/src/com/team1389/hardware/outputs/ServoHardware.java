package com.team1389.hardware.outputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.inputs.OpenRangeInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.registry.Constructor;
import com.team1389.hardware.registry.PWMPort;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Servo;

/**
 * A Servo motor
 * @author Jacob Prinz
 */
public class ServoHardware implements Watchable{
	public static final Constructor<PWMPort, ServoHardware> constructor = (PWMPort port) -> {
		return new ServoHardware(port);
	};
	
	Servo wpiServo;
	
	/**
	 * @param port PWM port that servo is plugged into
	 */
	private ServoHardware(PWMPort port) {
		wpiServo = new Servo(port.number);
	}
	
	public int getPort(){
		return wpiServo.getChannel();
	}
	
	public OpenRangeOutput getPositionOutput(){
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				wpiServo.set(val);
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 1;
			}
		};
	}

	@Override
	public String getName() {
		return "Servo " + getPort();
	}

	@Override
	public Map<String, String> getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("position", "" + wpiServo.getPosition());
		return info;
	}
	
	public OpenRangeInput getPositionInput(){
		return new OpenRangeInput() {

			@Override
			public double get() {
				return wpiServo.getPosition();
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 1;
			}
		};
	}
}
