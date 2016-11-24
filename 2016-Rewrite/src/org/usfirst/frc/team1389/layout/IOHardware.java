package org.usfirst.frc.team1389.layout;

import com.team1389.hardware.humaninputs.JoystickController;
import com.team1389.hardware.inputs.SwitchHardware;
import com.team1389.hardware.outputs.CANTalonGroup;
import com.team1389.hardware.outputs.CANTalonHardware;
import com.team1389.hardware.outputs.VictorHardware;
import com.team1389.hardware.registry.Registry;

public class IOHardware extends IOLayout {
	public IOHardware() {
		registry = new Registry();
		leftA = new CANTalonHardware(leftMotorA_CAN, registry);
		leftA.setInverted(true);
		leftB = new CANTalonHardware(leftMotorB_CAN, registry);
		leftC = new CANTalonHardware(leftMotorC_CAN, registry);
		leftDrive = new CANTalonGroup(leftA, leftB, leftC);

		rightA = new CANTalonHardware(rightMotorA_CAN, registry);
		rightB = new CANTalonHardware(rightMotorB_CAN, registry);
		rightC = new CANTalonHardware(rightMotorC_CAN, registry);
		rightDrive = new CANTalonGroup(rightA, rightB, rightC);

		elevationA = new CANTalonHardware(elevatorMotorA_CAN, registry);
		elevationB = new CANTalonHardware(elevatorMotorB_CAN, registry);

		elevation = new CANTalonGroup(elevationA, elevationB);

		IRsensor1 = new SwitchHardware(ballHolderIR1_DIO, registry);
		IRsensor2 = new SwitchHardware(ballHolderIR2_DIO, registry);
		IRsensor1.invert(true);
		IRsensor2.invert(true);
		IRsensors = SwitchHardware.combineSwitchOR(IRsensor1, IRsensor2);

		intake = new VictorHardware(intakeMotor_PWM, registry);
		intake.invert(true);

		driveJoystick = new JoystickController(driveJoystickPort);
		manipJoystick = new JoystickController(manipJoystickPort);

	}
}
