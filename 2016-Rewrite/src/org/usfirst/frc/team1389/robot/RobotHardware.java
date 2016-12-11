package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.GyroHardware;
import com.team1389.hardware.inputs.hardware.NavXHardware;
import com.team1389.hardware.inputs.hardware.PotentiometerHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.inputs.interfaces.BinaryInput;
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
		turretAngle = turretGyro.getAngleInput().sumInputs(navX.getAngleInput()).setRange(-180, 180).getWrapped();
		turret = registry.add(new CAN(can_TURRET_MOTOR), new CANTalonHardware());
	}

	private void initArm() {
		elevationA = registry.add(new CAN(can_ARM_PITCH_MOTOR_A), new CANTalonHardware());
		elevationB = registry.add(new CAN(can_ARM_PITCH_MOTOR_B), new CANTalonHardware());
		elevation = new CANTalonGroup(elevationA, elevationB);
		armPot = registry.add(new Analog(anlg_ARM_POTENTIOMETER), new PotentiometerHardware());
	}

	private void initIntake() {
		IRsensor1 = registry.add(new DIO(dio_INTAKE_IR_A), new SwitchHardware());
		IRsensor2 = registry.add(new DIO(dio_INTAKE_IR_B), new SwitchHardware());
		IRsensor1.invert(true);
		IRsensor2.invert(true);
		IRsensors = BinaryInput.combineOR(IRsensor1.getRawSwitch(), IRsensor2.getRawSwitch());
		intake = registry.add(new PWM(pwm_INTAKE_MOTOR), new VictorHardware());
		intake.invert(true);
	}

	private void initDriveTrain() {
		leftA = registry.add(new CAN(can_LEFT_MOTOR_A), new CANTalonHardware());
		leftA.setInverted(true);

		leftB = registry.add(new CAN(can_LEFT_MOTOR_B), new CANTalonHardware());
		leftC = registry.add(new CAN(can_LEFT_MOTOR_C), new CANTalonHardware());
		leftDrive = new CANTalonGroup(leftA, leftB, leftC);

		rightA = registry.add(new CAN(can_RIGHT_MOTOR_A), new CANTalonHardware());
		rightA.setInverted(true);

		rightB = registry.add(new CAN(can_RIGHT_MOTOR_B), new CANTalonHardware());
		rightC = registry.add(new CAN(can_RIGHT_MOTOR_C), new CANTalonHardware());
		rightDrive = new CANTalonGroup(leftA, leftB, leftC);
	}
}
