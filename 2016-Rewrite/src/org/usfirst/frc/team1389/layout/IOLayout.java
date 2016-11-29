package org.usfirst.frc.team1389.layout;
import com.team1389.hardware.humaninputs.JoystickController;
import com.team1389.hardware.inputs.hardware.NavXHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.outputs.hardware.CANTalonGroup;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;

public class IOLayout extends RobotMap{
	public Registry registry;
	
	public SwitchHardware IRsensor1;
	public SwitchHardware IRsensor2;
	public BooleanSource IRsensors;
	
	CANTalonHardware leftA;
	CANTalonHardware leftB;
	CANTalonHardware leftC;
	public CANTalonGroup leftDrive;
	public NavXHardware navX;
	
	CANTalonHardware rightA;
	CANTalonHardware rightB;
	CANTalonHardware rightC;
	public CANTalonGroup rightDrive;
	
	CANTalonHardware elevationA;
	CANTalonHardware elevationB;
	public CANTalonGroup elevation;
	
	public CANTalonHardware turret;
	public VictorHardware intake;
	
	public JoystickController manipJoystick;
	public JoystickController driveJoystick;
}
