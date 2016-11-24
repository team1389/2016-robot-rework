package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.IOHardware;
import org.usfirst.frc.team1389.systems.ArmSystem;
import org.usfirst.frc.team1389.systems.ArmSystem.ArmLocation;
import org.usfirst.frc.team1389.systems.IntakeSystem;
import org.usfirst.frc.team1389.util.ButtonEnumMap;

import com.team1389.hardware.configuration.PIDConstants;
import com.team1389.hardware.configuration.PIDController;
import com.team1389.hardware.control.PIDConfiguration;
import com.team1389.hardware.inputs.DigitalInput;
import com.team1389.hardware.inputs.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.LatchedDigitalInput;
import com.team1389.hardware.interfaces.inputs.OpenRangeInput;
import com.team1389.hardware.interfaces.inputs.PercentRangeInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput.WatchableOpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.watch.Watcher;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.SystemManager;
import com.team1389.system.System;

public class TeleopMain {
	IOHardware robot;
	SystemManager manager;
	Watcher debuggingPanel;
	
	public TeleopMain(IOHardware robot) {
		this.robot = robot;
		debuggingPanel = new Watcher();
	}

	public void teleopInit() {
		System driveSystem = setupDriveSystem();
		System armSystem = setupArmSystem();
		System intakeSystem=setupIntakeSystem();
		manager = new SystemManager(driveSystem,intakeSystem,armSystem);
		manager.init();
		debuggingPanel.watch(driveSystem,armSystem,intakeSystem,robot.IRsensor1,robot.IRsensor2);

	}
	public void teleopPeriodic() {
		manager.update();
		debuggingPanel.display();
	}
	public void teleopDisabled(){
		debuggingPanel.display();
	}
	public System setupArmSystem() {
		WatchableOpenRangeOutput elevator = OpenRangeOutput.generateWatchable(robot.elevation
				.getPositionOutput(new PIDConfiguration(new PIDConstants(.8, 0, 0), false, false)), "elevator");
		robot.elevation.getLeader().getWrappedTalon();
		LatchedDigitalInput armDownButton = (LatchedDigitalInput) robot.manipJoystick.getButton(1, InputStyle.LATCHED);
		LatchedDigitalInput armMidButton = (LatchedDigitalInput) robot.manipJoystick.getButton(2, InputStyle.LATCHED);
		LatchedDigitalInput armUpButton = (LatchedDigitalInput) robot.manipJoystick.getButton(3, InputStyle.LATCHED);
		LatchedDigitalInput armTopButton = (LatchedDigitalInput) robot.manipJoystick.getButton(4, InputStyle.LATCHED);
		ButtonEnumMap<ArmLocation> map=new ButtonEnumMap<>(ArmLocation.DOWN);
				map.setMappings(
				map.new ButtonEnum(armDownButton,ArmLocation.DOWN),
				map.new ButtonEnum(armMidButton,ArmLocation.DEFENSE),
				map.new ButtonEnum(armUpButton,ArmLocation.HIGH_GOAL),
				map.new ButtonEnum(armTopButton,ArmLocation.LOW_GOAL));
		OpenRangeInput armVal=robot.elevation.getLeader().getPositionInput();
		ArmSystem armSystem=new ArmSystem(elevator,map,armVal);
		debuggingPanel.watch(elevator);
		return armSystem;
	}
	public System setupIntakeSystem(){
		PercentRangeOutput motor=robot.intake.getVoltageOutput();
		PercentRangeInput joy=robot.manipJoystick.getAxis(1);
		DigitalInput IRsensors=DigitalInput.createInput(robot.IRsensors, InputStyle.RAW);
		DigitalInput manualOverride=robot.manipJoystick.getButton(9, InputStyle.RAW);
		return new IntakeSystem(motor, joy, IRsensors, manualOverride);
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
