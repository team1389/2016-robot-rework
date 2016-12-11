package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotHardware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.ArmSystem;
import org.usfirst.frc.team1389.systems.ArmSystem.ArmLocation;
import org.usfirst.frc.team1389.systems.IntakeSystem;
import org.usfirst.frc.team1389.systems.TurretSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.System;
import com.team1389.system.SystemManager;

public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotHardware robot;

	public TeleopMain(RobotHardware robot) {
		this.robot = robot;
	}

	public void init() {
		controls = ControlBoard.getInstance();
		System driveSystem = setupDriveSystem();
		System armSystem = setupArmSystem();
		System intakeSystem = setupIntakeSystem();
		System turretSystem = setupTurretSystem();

		manager = new SystemManager(driveSystem, intakeSystem, armSystem, turretSystem);
		manager.init();
		DebugDash.getInstance().watch(driveSystem, armSystem, intakeSystem, turretSystem);

	}

	public void periodic() {
		manager.update();
	}

	private TurretSystem setupTurretSystem() {
		PercentOut turretVoltage = robot.turret.getVoltageOutput();
		TurretSystem turret = new TurretSystem(turretVoltage, robot.turretAngle, controls.getTurretManual(),
				controls.getTurretZero());
		return turret;
	}

	public ArmSystem setupArmSystem() {
		RangeOut<Percent> elevator = robot.elevation.getVoltageOutput();
		ButtonEnumMap<ArmLocation> map = new ButtonEnumMap<>(ArmLocation.DOWN);
		map.setMappings(map.new ButtonEnum(controls.getArmPositionA(), ArmLocation.DOWN),
				map.new ButtonEnum(controls.getArmPositionB(), ArmLocation.DEFENSE),
				map.new ButtonEnum(controls.getArmPositionC(), ArmLocation.VERTICAL),
				map.new ButtonEnum(controls.getArmPositionD(), ArmLocation.LOW_GOAL));
		RangeIn<Position> armVal = robot.armPot.getAnalogInput().mapToRange(120, 0).setRange(0, 360);
		ArmSystem armSystem = new ArmSystem(elevator, map, armVal);
		return armSystem;
	}

	public System setupIntakeSystem() {
		PercentOut motor = robot.intake.getVoltageOutput();
		BinaryInput IRsensors = robot.IRsensors;
		return new IntakeSystem(motor, IRsensors, controls.getIntakeAxis(), controls.getIntakeOverride());
	}

	public System setupDriveSystem() {
		PercentOut left = robot.leftDrive.getVoltageOutput();
		PercentOut right = robot.rightDrive.getVoltageOutput().invert();

		return new CheesyDriveSystem(left, right, controls.getThrottle(), new PercentIn(controls.getWheel().invert()),
				controls.getQuickTurn());
	}
}
