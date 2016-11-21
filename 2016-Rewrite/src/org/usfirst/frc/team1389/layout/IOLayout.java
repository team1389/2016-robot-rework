package org.usfirst.frc.team1389.layout;
import com.team1389.hardware.humaninputs.JoystickController;
import com.team1389.hardware.outputs.CANTalonGroup;
import com.team1389.hardware.outputs.CANTalonHardware;
import com.team1389.hardware.registry.Registry;

public class IOLayout {
	public Registry registry;
	
	
	CANTalonHardware leftA;
	CANTalonHardware leftB;
	CANTalonHardware leftC;
	public CANTalonGroup leftDrive;

	CANTalonHardware rightA;
	CANTalonHardware rightB;
	CANTalonHardware rightC;
	public CANTalonGroup rightDrive;
	
	CANTalonHardware elevationA;
	CANTalonHardware elevationB;
	public CANTalonGroup elevation;
	
	public JoystickController manipJoystick;
	public JoystickController driveJoystick;
}
