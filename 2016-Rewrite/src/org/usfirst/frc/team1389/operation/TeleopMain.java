package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotHardware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.ArmSystem;
import org.usfirst.frc.team1389.systems.ArmSystem.ArmLocation;
import org.usfirst.frc.team1389.systems.IntakeSystem;
import org.usfirst.frc.team1389.systems.TurretSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CheesyDriveSystem;

public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotHardware robot;

	public TeleopMain(RobotHardware robot) {
		this.robot = robot;
	}

	public void init() {
		controls = ControlBoard.getInstance();
		Subsystem driveSystem = setupDriveSystem();
		Subsystem armSystem = setupArmSystem();
		Subsystem intakeSystem = setupIntakeSystem();
		Subsystem turretSystem = setupTurretSystem();

		manager = new SystemManager(driveSystem, intakeSystem, armSystem, turretSystem);
		manager.init();
		DebugDash.getInstance().watch(driveSystem, armSystem, intakeSystem, turretSystem);

	}

	public void periodic() {
		manager.update();
	}

	private TurretSystem setupTurretSystem() {
		PercentOut turretVoltage = robot.turret.getVoltageOutput();
		TurretSystem turret = new TurretSystem(turretVoltage, robot.turretAngle, controls.turretAxis,
				controls.turretZero);
		return turret;
	}

	public ArmSystem setupArmSystem() {
		PercentOut elevator = robot.elevation.getVoltageOutput();
		ButtonEnumMap<ArmLocation> map = new ButtonEnumMap<>(ArmLocation.DOWN);
		map.setMappings(map.new ButtonEnum(controls.armButtonA, ArmLocation.DOWN),
				map.new ButtonEnum(controls.armButtonB, ArmLocation.DEFENSE),
				map.new ButtonEnum(controls.armButtonC, ArmLocation.VERTICAL),
				map.new ButtonEnum(controls.armButtonD, ArmLocation.LOW_GOAL));
		AngleIn armVal = robot.armPot.getAnalogInput().mapToRange(120, 0).setRange(0, 76).mapToRange(0, 90)
				.setRange(0, 360).mapToAngle();
		ArmSystem armSystem = new ArmSystem(elevator, map, armVal);
		return armSystem;
	}

	public Subsystem setupIntakeSystem() {
		PercentOut motor = robot.intake.getVoltageOutput();
		DigitalIn IRsensors = robot.IRsensors;
		return new IntakeSystem(motor, IRsensors, controls.intakeAxis, controls.intakeOverride);
	}

	public Subsystem setupDriveSystem() {
		PercentOut left = robot.leftDrive.getVoltageOutput();
		PercentOut right = robot.rightDrive.getVoltageOutput().invert();

		return new CheesyDriveSystem(left, right, controls.throttle, controls.wheel.invert(), controls.quickTurn);
	}
}
