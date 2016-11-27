package com.team1389.hardware.humaninputs;

import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.inputs.interfaces.POVInput;
import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.software.PercentIn;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

public class JoystickController{
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
	public PercentIn getAxis(int axis){
		return new PercentIn(() -> {
			return wpiJoystick.getRawAxis(axis);
		});
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
}
