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
	protected static final int can_LEFT_MOTOR_A = 4; protected static final boolean leftMotorA_isInverted = true;
	protected static final int can_LEFT_MOTOR_B = 5; protected static final boolean leftMotorB_isInverted = true;
	protected static final int can_LEFT_MOTOR_C = 6; protected static final boolean leftMotorC_isInverted = true;
	protected static final int can_RIGHT_MOTOR_A = 2; protected static final boolean rightMotorA_isInverted = false;
	protected static final int can_RIGHT_MOTOR_B = 1; protected static final boolean rightMotorB_isInverted = false;
	protected static final int can_RIGHT_MOTOR_C = 3; protected static final boolean rightMotorC_isInverted = false;
	
	//Arm
	protected static final int can_TURRET_MOTOR = 10; protected static final boolean turntableMotor_isInverted = false;
	protected static final int anlg_TURRET_GYRO=1;
	
	protected static final int can_ARM_PITCH_MOTOR_A = 12; protected static final boolean elevatorMotorA_isInverted = true;
	protected static final int can_ARM_PITCH_MOTOR_B = 8; protected static final boolean elevatorMotorB_isInverted = true;
	protected static final int anlg_ARM_POTENTIOMETER=0;
	
	//Ball manipulator
	protected static final int pwm_INTAKE_MOTOR = 0; protected static final boolean intakeMotor_isInverted = true;
	protected static final int can_FLYWHEEL_MOTOR_A = 9; protected static final boolean flywheelMotorA_isInverted = true;
	protected static final int can_FLYWHEEL_MOTOR_B = 7; protected static final boolean flywheelMotorB_isInverted = false;
	
	//Inputs
	protected static final int dio_INTAKE_IR_A = 0; protected static final boolean ballHolderIR1_isInverted = false;
	protected static final int dio_INTAKE_IR_B = 1; protected static final boolean ballHolderIR2_isInverted = false;
	
	
	protected static final boolean leftEncoderInverted = true;
	protected static final boolean rightEncoderInverted = false;
	protected static final boolean elevatorEncoderInverted = true;
	protected static final boolean turntableEncoderInverted = false;
	protected static final boolean flywheelEncoderInverted = false;	
}

