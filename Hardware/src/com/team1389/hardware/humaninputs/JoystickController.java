package com.team1389.hardware.humaninputs;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.team1389.hardware.interfaces.inputs.BooleanSource;
import com.team1389.hardware.interfaces.inputs.DigitalInput;
import com.team1389.hardware.interfaces.inputs.DigitalInput.InputStyle;
import com.team1389.hardware.interfaces.inputs.POVInput;
import com.team1389.hardware.interfaces.inputs.PercentRangeInput;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class JoystickController implements Watchable{
	final Joystick wpiJoystick;

	public JoystickController(int port) {
		wpiJoystick = new Joystick(port);
	}
	
	public DigitalInput getButton(int button,InputStyle style){
		return DigitalInput.createInput(getRawButton(button),style);
	}
	public BooleanSource getRawButton(int button){
		return ()->{
			return wpiJoystick.getRawButton(button);
		};
	}
	public PercentRangeInput getAxis(int axis){
		return () -> {
			return wpiJoystick.getRawAxis(axis);
		};
	}
	
	public POVInput getPov(){
		return () -> {
			return wpiJoystick.getPOV();
		};
	}
	
	/**
	 * @param left intensity from 0 to 1
	 * @param right intensity from 0 to 1
	 */
	public void setRumble(double left, double right){
		setLeftRumble(left);
		setRightRumble(right);
	}
	
	/**
	 * @param strength intensity of rumble from 0 to 1
	 */
	public void setLeftRumble(double strength){
		wpiJoystick.setRumble(RumbleType.kLeftRumble, (float)strength);
	}
	
	/**
	 * @param strength intensity of rumble from 0 to 1
	 */
	public void setRightRumble(double strength){
		wpiJoystick.setRumble(RumbleType.kRightRumble, (float)strength);
	}

	//things to make this class watchable
	@Override
	public String getName() {
		return "Joystick " + wpiJoystick.getName();
	}

	@Override
	public Map<String, String> getInfo() {
		int numButtons = wpiJoystick.getButtonCount();
		int numAxes = wpiJoystick.getAxisCount();
		int numPov = wpiJoystick.getPOVCount();
		
		Map<String, String> info = new HashMap<>();

		IntStream.range(1, numButtons + 1).forEachOrdered((int port) -> {
			info.put("Button " + port, "" + wpiJoystick.getRawButton(port));
		});
		
		IntStream.range(0, numAxes).forEachOrdered((int num) -> {
			info.put("Axis " + num, "" + wpiJoystick.getRawAxis(num));
		});
		
		IntStream.range(0, numPov).forEachOrdered((int num) -> {
			info.put("POV " + num, "" + wpiJoystick.getPOV(num));
		});
		
		
		return info;
	}
}
