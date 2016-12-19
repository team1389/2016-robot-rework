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
	private static RobotHardware mInstance = new RobotHardware();

	/**
	 * Returns the initialized instance of the set all robot hardware
	 * 
	 * @return an initialized instance of RobotHardware
	 */
	public static RobotHardware getInstance() {
		return mInstance;
	}

	/**
	 * Initializes robot hardware by subsystem. <br>
	 * note: use this method as an index to show hardware initializations that occur, and to find the init code for a particular system's hardware
	 */
	private RobotHardware() {
		registry = new Registry();
		navX = new NavXHardware(SPI.Port.kMXP, registry);
		initDriveTrain();
		initArm();
		initTurret();
		initIntake();
	}

	private void initTurret() {
		turretGyro = registry.add(new Analog(anlg_TURRET_GYRO), new GyroHardware());
		turret = registry.add(new CAN(can_TURRET_MOTOR), new CANTalonHardware(inv_TURRET_MOTOR));
		turretAngle = turretGyro.getAngleInput().sumInputs(navX.getAngleInput()).setRange(-180, 180).getWrapped();
	}

	private void initArm() {
		elevationA = registry.add(new CAN(can_ELEVATOR_MOTOR_A),new CANTalonHardware(inv_ELEVATOR_MOTOR_A));
		elevationB = registry.add(new CAN(can_ELEVATOR_MOTOR_B), new CANTalonHardware(inv_ELEVATOR_MOTOR_B));
		armPot = registry.add(new Analog(anlg_ARM_POTENTIOMETER), new PotentiometerHardware());

		elevation = new CANTalonGroup(elevationA, elevationB);
	}

	private void initIntake() {
		IRsensor1 = registry.add(new DIO(dio_INTAKE_IR_A), new SwitchHardware(sinv_INTAKE_IR_A));
		IRsensor2 = registry.add(new DIO(dio_INTAKE_IR_B), new SwitchHardware(sinv_INTAKE_IR_B));
		intake = registry.add(new PWM(pwm_INTAKE_MOTOR), new VictorHardware(inv_INTAKE_MOTOR));

		IRsensors = IRsensor1.getRawSwitch().combineOR(IRsensor2.getRawSwitch());
	}

	private void initDriveTrain() {
		leftA = registry.add(new CAN(can_LEFT_MOTOR_A), new CANTalonHardware(inv_LEFT_MOTOR_A, sinv_LEFT_ENCODER));
		leftB = registry.add(new CAN(can_LEFT_MOTOR_B), new CANTalonHardware(inv_LEFT_MOTOR_B));
		leftC = registry.add(new CAN(can_LEFT_MOTOR_C), new CANTalonHardware(inv_LEFT_MOTOR_C));
		rightA = registry.add(new CAN(can_RIGHT_MOTOR_A), new CANTalonHardware(inv_RIGHT_MOTOR_A, sinv_RIGHT_ENCODER));
		rightB = registry.add(new CAN(can_RIGHT_MOTOR_B), new CANTalonHardware(inv_RIGHT_MOTOR_B));
		rightC = registry.add(new CAN(can_RIGHT_MOTOR_C), new CANTalonHardware(inv_RIGHT_MOTOR_C));

		leftDrive = new CANTalonGroup(leftA, leftB, leftC);
		rightDrive = new CANTalonGroup(rightA, rightB, rightC);
	}
}
