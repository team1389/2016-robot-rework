package org.usfirst.frc.team1389.robot.controls;

import com.team1389.hardware.inputs.hardware.JoystickHardware;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.DigitalIn.InputFilter;
import com.team1389.hardware.inputs.software.PercentIn;

/**
 * A basic framework for the robot controls. Like the RobotHardware, one instance of the ControlBoard object is created upon startup, then other methods request the singleton ControlBoard instance.
 * 
 * @author amind
 * @see ControlMap
 */
public class ControlBoard extends ControlMap {
	private static ControlBoard mInstance = new ControlBoard();

	public static ControlBoard getInstance() {
		return mInstance;
	}

	private ControlBoard() {
	}

	private final JoystickHardware driveController = new JoystickHardware(DRIVE_CONTROLLER);
	private final JoystickHardware manipController = new JoystickHardware(MANIP_CONTROLLER);

	// DRIVER CONTROLS
	public PercentIn throttle = driveController.getAxis(ax_THROTTLE_AXIS).applyDeadband(.02);
	public PercentIn wheel = driveController.getAxis(ax_WHEEL_AXIS).applyDeadband(.02);
	public DigitalIn quickTurn = driveController.getButton(btn_QUICK_TURN);

	// MANIPULATOR CONTROLS
	public DigitalIn armButtonA = manipController.getButton(btn_ARM_POSITION_A).getLatched();
	public DigitalIn armButtonB = manipController.getButton(btn_ARM_POSITION_B).getLatched();
	public DigitalIn armButtonC = manipController.getButton(btn_ARM_POSITION_C).getLatched();
	public DigitalIn armButtonD = manipController.getButton(btn_ARM_POSITION_D).getLatched();

	public DigitalIn turretZero = manipController.getButton(btn_TURRET_ZERO, InputFilter.LATCHED);
	public PercentIn turretAxis = manipController.getAxis(ax_TURRET_AXIS);
	public PercentIn intakeAxis = manipController.getAxis(ax_INTAKE_AXIS);
	public DigitalIn intakeOverride = manipController.getButton(btn_INTAKE_MANUAL_OVERRIDE);
}
