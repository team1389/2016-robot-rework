package org.usfirst.frc.team1389.layout;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//Outputs:
	//Drivetrain
	public static final int leftMotorA_CAN = 6; public static final boolean leftMotorA_isInverted = true;
	public static final int leftMotorB_CAN = 4; public static final boolean leftMotorB_isInverted = true;
	public static final int leftMotorC_CAN = 5; public static final boolean leftMotorC_isInverted = true;
	public static final int rightMotorA_CAN = 1; public static final boolean rightMotorA_isInverted = false;
	public static final int rightMotorB_CAN = 2; public static final boolean rightMotorB_isInverted = false;
	public static final int rightMotorC_CAN = 3; public static final boolean rightMotorC_isInverted = false;
	//Arm
	public static final int turntableMotor_CAN = 10; public static final boolean turntableMotor_isInverted = false;
	public static final int elevatorMotorA_CAN = 12; public static final boolean elevatorMotorA_isInverted = true;
	public static final int elevatorMotorB_CAN = 8; public static final boolean elevatorMotorB_isInverted = true;
	//Ball Manipulator
	public static final int intakeMotor_CAN = 0; public static final boolean intakeMotor_isInverted = true;
	public static final int ballHolderIR1_DIO = 7; public static final boolean ballHolderIR1_isInverted = false;
	public static final int ballHolderIR2_DIO = 1; public static final boolean ballHolderIR2_isInverted = false;

	public static final int flywheelMotorA_CAN = 9; public static final boolean flywheelMotorA_isInverted = true;
	public static final int flywheelMotorB_CAN = 7; public static final boolean flywheelMotorB_isInverted = false;
	
	//Inputs:
	public static final boolean leftEncoderInverted = true;
	public static final boolean rightEncoderInverted = false;
	public static final boolean elevatorEncoderInverted = true;
	public static final boolean turntableEncoderInverted = false;
	public static final boolean flywheelEncoderInverted = false;
	
	
	public static final int ringLightA_Sol=0;
	public static final int ringLightB_Sol=1;
	//Misc
	public static final int driveJoystickPort = 0;
	public static final int manipJoystickPort = 1;
	
}

