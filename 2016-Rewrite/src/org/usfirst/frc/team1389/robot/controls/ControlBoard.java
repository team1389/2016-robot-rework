package org.usfirst.frc.team1389.robot.controls;

import com.team1389.hardware.humaninputs.JoystickController;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.DigitalIn.InputType;
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

	private final JoystickController driveController;
	private final JoystickController manipController;

	private ControlBoard() {
		driveController = new JoystickController(DRIVE_CONTROLLER);
		manipController = new JoystickController(MANIP_CONTROLLER);
	}

	// DRIVER CONTROLS

	/**
	 * axis used to control the forward speed of the drivetrain
	 * 
	 * @return an axis on the driver controller
	 */
	public PercentIn getThrottle() {
		return driveController.getAxis(ax_THROTTLE_AXIS).applyDeadband(.02);
	}

	/**
	 * axis used to control the turning of the drivetrain
	 * 
	 * @return an axis on the driver controller
	 */
	public PercentIn getWheel() {
		return driveController.getAxis(ax_WHEEL_AXIS).applyDeadband(.02);
	}

	/**
	 * button when <b>held down</b> switches turning from Curvature mode to regular Tank turning
	 * 
	 * @return a button on the driver controller
	 * @see com.team1389.system.CheesyDriveSystem
	 */
	public DigitalIn getQuickTurn() {
		return driveController.getButton(btn_QUICK_TURN);
	}

	// MANIPULATOR CONTROLS

	/**
	 * button when <b>pressed</b> sets the arm to position A
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getArmPositionA() {
		return manipController.getButton(btn_ARM_POSITION_A).getLatched();
	}

	/**
	 * button when <b>pressed</b> sets the arm to position B
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getArmPositionB() {
		return manipController.getButton(btn_ARM_POSITION_B).getLatched();
	}

	/**
	 * button when <b>pressed</b> sets the arm to position C
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getArmPositionC() {
		return manipController.getButton(btn_ARM_POSITION_C).getLatched();
	}

	/**
	 * button when <b>pressed</b> sets the arm to position D
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getArmPositionD() {
		return manipController.getButton(btn_ARM_POSITION_D).getLatched();
	}

	/**
	 * button when <b>pressed</b> overrides the manual turret control and returns the turret to zero
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getTurretZero() {
		return manipController.getButton(btn_TURRET_ZERO, InputType.LATCHED);
	}

	/**
	 * axis used to control speed of turret manually
	 * 
	 * @return an axis on the manipulator joystick
	 */
	public PercentIn getTurretManual() {
		return manipController.getAxis(ax_TURRET_AXIS);
	}

	/**
	 * axis used to control speed of intake wheels
	 * 
	 * @return an axis on the manipulator joystick
	 */
	public PercentIn getIntakeAxis() {
		return manipController.getAxis(ax_INTAKE_AXIS);
	}

	/**
	 * button when <b>held down</b> overrides the automatic intake control and defers to {@link #getIntakeAxis()}
	 * 
	 * @return a button on the manipulator joystick
	 */
	public DigitalIn getIntakeOverride() {
		return manipController.getButton(btn_INTAKE_MANUAL_OVERRIDE);
	}
}
