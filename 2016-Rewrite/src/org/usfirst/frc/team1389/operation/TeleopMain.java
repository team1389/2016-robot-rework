package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.IOHardware;
import org.usfirst.frc.team1389.systems.ArmSystem;

import com.team1389.hardware.configuration.PIDConstants;
import com.team1389.hardware.control.PIDConfiguration;
import com.team1389.hardware.interfaces.inputs.DigitalInput;
import com.team1389.hardware.interfaces.inputs.DigitalInput.InputStyle;
import com.team1389.hardware.interfaces.inputs.LatchedDigitalInput;
import com.team1389.hardware.interfaces.inputs.PercentRangeInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.watch.Watchable;
import com.team1389.hardware.watch.Watcher;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.System;
import com.team1389.system.SystemManager;

public class TeleopMain {
	IOHardware robot;
	SystemManager manager;
	Watcher debuggingPanel;
	
	public TeleopMain(IOHardware robot) {
		this.robot = robot;
		debuggingPanel = new Watcher();
	}

	public void teleopInit() {
		manager = new SystemManager(setupArmSystem(), setupDriveSystem());
		manager.init();
	}

	public void teleopPeriodic() {
		manager.update();
		debuggingPanel.display();
	}

	public System setupArmSystem() {
		OpenRangeOutput elevator = robot.elevation
				.getPositionOutput(new PIDConfiguration(new PIDConstants(1, 0, 0), false, false));
		
		LatchedDigitalInput liftArmButton = (LatchedDigitalInput) robot.manipJoystick.getButton(1, InputStyle.LATCHED);
		LatchedDigitalInput lowerArmButton = (LatchedDigitalInput) robot.manipJoystick.getButton(2, InputStyle.LATCHED);
		ArmSystem armSystem=new ArmSystem(elevator, liftArmButton, lowerArmButton);
		debuggingPanel.watch(armSystem);
		return armSystem;
	}

	public System setupDriveSystem() {
		PercentRangeOutput left = robot.leftDrive.getVoltageOutput();
		PercentRangeOutput right = robot.rightDrive.getVoltageOutput();

		PercentRangeInput throttle = PercentRangeInput.applyDeadband(robot.driveJoystick.getAxis(1), .02);
		PercentRangeInput wheel = PercentRangeInput.applyDeadband(robot.driveJoystick.getAxis(0), .02);
		DigitalInput quickTurnButton = robot.driveJoystick.getButton(1, InputStyle.RAW);

		return new CheesyDriveSystem(left, right, throttle, wheel, quickTurnButton);
	}

}
