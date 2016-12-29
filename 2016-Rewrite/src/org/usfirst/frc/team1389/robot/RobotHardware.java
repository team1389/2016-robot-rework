package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.GyroHardware;
import com.team1389.hardware.inputs.hardware.NavXHardware;
import com.team1389.hardware.inputs.hardware.PotentiometerHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonGroup;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.hardware.registry.port_types.PWM;

import edu.wpi.first.wpilibj.SPI;

/**
 * responsible for initializing and storing hardware objects defined in {@link RobotLayout}
 * 
 * @author amind
 * @see RobotLayout
 * @see RobotMap
 */
public class RobotHardware extends RobotLayout {

	/**
	 * Initializes robot hardware by subsystem. <br>
	 * note: use this method as an index to show hardware initializations that occur, and to find the init code for a particular system's hardware
	 */
	protected RobotHardware() {
		registry = new Registry();
		navX = new NavXHardware(SPI.Port.kMXP, registry);
		initDriveTrain();
		initArm();
		initTurret();
		initIntake();
	}

	public Registry getRegistry() {
		return registry;
	}

	private void initTurret() {
		turretGyro = new GyroHardware(new Analog(anlg_TURRET_GYRO), registry);
		turret = new CANTalonHardware(inv_TURRET_MOTOR, new CAN(can_TURRET_MOTOR), registry);
	}

	private void initArm() {
		elevationA = new CANTalonHardware(inv_ELEVATOR_MOTOR_A, new CAN(can_ELEVATOR_MOTOR_A), registry);
		elevationB = new CANTalonHardware(inv_ELEVATOR_MOTOR_B, new CAN(can_ELEVATOR_MOTOR_B), registry);
		armPot = new PotentiometerHardware(new Analog(anlg_ARM_POTENTIOMETER), registry);
		elevation = new CANTalonGroup(elevationA, elevationB);
	}

	private void initIntake() {
		IRsensor1 = new SwitchHardware(sinv_INTAKE_IR_A, new DIO(dio_INTAKE_IR_A), registry);
		IRsensor2 = new SwitchHardware(sinv_INTAKE_IR_B, new DIO(dio_INTAKE_IR_B), registry);
		intake = new VictorHardware(inv_INTAKE_MOTOR, new PWM(pwm_INTAKE_MOTOR), registry);
	}

	private void initDriveTrain() {
		leftA = new CANTalonHardware(sinv_LEFT_ENCODER, inv_LEFT_MOTOR_A, new CAN(can_LEFT_MOTOR_A), registry);
		leftB = new CANTalonHardware(inv_LEFT_MOTOR_B, new CAN(can_LEFT_MOTOR_B), registry);
		leftC = new CANTalonHardware(inv_LEFT_MOTOR_C, new CAN(can_LEFT_MOTOR_C), registry);
		rightA = new CANTalonHardware(sinv_RIGHT_ENCODER, inv_RIGHT_MOTOR_A, new CAN(can_RIGHT_MOTOR_A), registry);
		rightA = new CANTalonHardware(inv_RIGHT_MOTOR_B, new CAN(can_RIGHT_MOTOR_B), registry);
		rightA = new CANTalonHardware(inv_RIGHT_MOTOR_C, new CAN(can_RIGHT_MOTOR_C), registry);

		leftDrive = new CANTalonGroup(leftA, leftB, leftC);
		rightDrive = new CANTalonGroup(rightA, rightB, rightC);
	}
}
