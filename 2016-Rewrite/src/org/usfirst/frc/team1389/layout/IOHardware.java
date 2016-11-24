package org.usfirst.frc.team1389.layout;

import com.team1389.hardware.humaninputs.JoystickController;
import com.team1389.hardware.inputs.SwitchHardware;
import com.team1389.hardware.outputs.CANTalonGroup;
import com.team1389.hardware.outputs.CANTalonHardware;
import com.team1389.hardware.outputs.VictorHardware;
import com.team1389.hardware.registry.Registry;

public class IOHardware extends IOLayout{
	public IOHardware(){
		registry=new Registry();
		leftA= registry.registerCANHardware(RobotMap.leftMotorA_CAN, CANTalonHardware.constructor);
		leftA.setInverted(true);
		leftB= registry.registerCANHardware(RobotMap.leftMotorB_CAN, CANTalonHardware.constructor);
		leftC= registry.registerCANHardware(RobotMap.leftMotorC_CAN, CANTalonHardware.constructor);
		leftDrive=new CANTalonGroup(leftA,leftB,leftC);
		
		rightA= registry.registerCANHardware(RobotMap.rightMotorA_CAN, CANTalonHardware.constructor);
		rightB= registry.registerCANHardware(RobotMap.rightMotorB_CAN, CANTalonHardware.constructor);
		rightC= registry.registerCANHardware(RobotMap.rightMotorC_CAN, CANTalonHardware.constructor);
		rightDrive=new CANTalonGroup(rightA,rightB,rightC);
		
		elevationA=registry.registerCANHardware(RobotMap.elevatorMotorA_CAN, CANTalonHardware.constructor);
		elevationA.getWrappedTalon().enableBrakeMode(false);
		elevationB=registry.registerCANHardware(RobotMap.elevatorMotorB_CAN, CANTalonHardware.constructor);
		elevationB.getWrappedTalon().enableBrakeMode(false);

		elevation=new CANTalonGroup(elevationA,elevationB);
		
		IRsensor1=registry.registerDIOHardware(RobotMap.ballHolderIR1_DIO, SwitchHardware.constructor);
		IRsensor2=registry.registerDIOHardware(RobotMap.ballHolderIR2_DIO, SwitchHardware.constructor);
		IRsensor1.invert(true);
		IRsensor2.invert(true);
		IRsensors=SwitchHardware.combineSwitchOR(IRsensor1,IRsensor2);
		
		intake=registry.registerPWMHardware(RobotMap.intakeMotor_PWM, VictorHardware.constructor);
		intake.invert(true);
		
		driveJoystick=new JoystickController(RobotMap.driveJoystickPort);
		manipJoystick=new JoystickController(RobotMap.manipJoystickPort);

	}
}
