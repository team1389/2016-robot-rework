package com.team1389.hardware.outputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.inputs.RangeIn;
import com.team1389.hardware.interfaces.outputs.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Servo;

/**
 * A Servo motor
 * @author Jacob Prinz
 */
public class ServoHardware implements Watchable{
	Servo wpiServo;
	
	/**
	 * @param port PWM port that servo is plugged into
	 */
		public ServoHardware(int pwmPort, Registry registry) {
		 		registry.claimPWMPort(pwmPort);
		 		registry.registerWatcher(this);
		
		 		wpiServo = new Servo(pwmPort);
		  	}
	public int getPort(){
		return wpiServo.getChannel();
	}
	
	public RangeOut getPositionOutput(){
		return new RangeOut() {

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
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("position", "" + wpiServo.getPosition());
		return null;
	}
	
	public RangeIn getPositionInput(){
		return new RangeIn() {

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
