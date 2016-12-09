package org.usfirst.frc.team1389.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//Outputs
	
	//Drivetrain
	protected static final int leftMotorA_CAN = 4; protected static final boolean leftMotorA_isInverted = true;
	protected static final int leftMotorB_CAN = 5; protected static final boolean leftMotorB_isInverted = true;
	protected static final int leftMotorC_CAN = 6; protected static final boolean leftMotorC_isInverted = true;
	protected static final int rightMotorA_CAN = 2; protected static final boolean rightMotorA_isInverted = false;
	protected static final int rightMotorB_CAN = 1; protected static final boolean rightMotorB_isInverted = false;
	protected static final int rightMotorC_CAN = 3; protected static final boolean rightMotorC_isInverted = false;
	
	//Arm
	protected static final int turretMotor_CAN = 10; protected static final boolean turntableMotor_isInverted = false;
	protected static final int turretGyro_ANALOG=1;
	
	protected static final int elevatorMotorA_CAN = 12; protected static final boolean elevatorMotorA_isInverted = true;
	protected static final int elevatorMotorB_CAN = 8; protected static final boolean elevatorMotorB_isInverted = true;
	protected static final int armPotentiometer_ANALOG=0;
	
	//Ball manipulator
	protected static final int intakeMotor_PWM = 0; protected static final boolean intakeMotor_isInverted = true;
	protected static final int flywheelMotorA_CAN = 9; protected static final boolean flywheelMotorA_isInverted = true;
	protected static final int flywheelMotorB_CAN = 7; protected static final boolean flywheelMotorB_isInverted = false;
	
	//Inputs
	protected static final int ballHolderIR1_DIO = 0; protected static final boolean ballHolderIR1_isInverted = false;
	protected static final int ballHolderIR2_DIO = 1; protected static final boolean ballHolderIR2_isInverted = false;
	
	
	protected static final boolean leftEncoderInverted = true;
	protected static final boolean rightEncoderInverted = false;
	protected static final boolean elevatorEncoderInverted = true;
	protected static final boolean turntableEncoderInverted = false;
	protected static final boolean flywheelEncoderInverted = false;	
}

