package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.controls.ControlBoard;
import org.usfirst.frc.team1389.layout.robot.RobotHardware;
import org.usfirst.frc.team1389.systems.ArmSystem;
import org.usfirst.frc.team1389.systems.ArmSystem.ArmLocation;
import org.usfirst.frc.team1389.systems.IntakeSystem;
import org.usfirst.frc.team1389.systems.TurretSystem;

import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.System;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopMain extends Operator {
	SystemManager manager;
	ControlBoard controls;

	public TeleopMain(RobotHardware robot) {
		super(robot);
	}

	protected void init() {
		controls = ControlBoard.getInstance();
		System driveSystem = setupDriveSystem();
		System armSystem = setupArmSystem();
		System intakeSystem = setupIntakeSystem();
		System turretSystem = setupTurretSystem();

		manager = new SystemManager(driveSystem, intakeSystem, armSystem, turretSystem);
		manager.init();
		debuggingPanel.watch(driveSystem, armSystem, intakeSystem, robot.IRsensor1, robot.IRsensor2);

	}

	protected void periodic() {
		manager.update();
		SmartDashboard.putNumber("pot", robot.armPot.get());
		debuggingPanel.publish(Watcher.DASHBOARD);
	}

	protected void disabled() {
		debuggingPanel.publish(Watcher.DASHBOARD);
	}

	private TurretSystem setupTurretSystem() {
		PercentOut turretVoltage = robot.turret.getVoltageOutput();
		RangeIn<Angle> turretAngle = robot.navX.getAngleInput();
		TurretSystem turret = new TurretSystem(turretVoltage, turretAngle, controls.getTurretManual(),
				controls.getTurretZero());
		return turret;
	}

	public ArmSystem setupArmSystem() {
		RangeOut<Position> elevator = robot.elevation
				.getPositionOutput(new PIDConfiguration(new PIDConstants(.8, 0, 0), false, false));
		ButtonEnumMap<ArmLocation> map = new ButtonEnumMap<>(ArmLocation.DOWN);
		map.setMappings(map.new ButtonEnum(controls.getArmPositionA(), ArmLocation.DOWN),
				map.new ButtonEnum(controls.getArmPositionB(), ArmLocation.DEFENSE),
				map.new ButtonEnum(controls.getArmPositionC(), ArmLocation.VERTICAL),
				map.new ButtonEnum(controls.getArmPositionD(), ArmLocation.LOW_GOAL));
		RangeIn<Position> armVal = robot.elevation.getLeader().getPositionInput();
		ArmSystem armSystem = new ArmSystem(elevator, map, armVal);
		return armSystem;
	}

	public System setupIntakeSystem() {
		PercentOut motor = robot.intake.getVoltageOutput();
		DigitalInput IRsensors = DigitalInput.createInput(robot.IRsensors, InputStyle.RAW);
		return new IntakeSystem(motor, IRsensors, controls.getIntakeAxis(), controls.getIntakeOverride());
	}

	public System setupDriveSystem() {
		PercentOut left = robot.leftDrive.getVoltageOutput();
		PercentOut right = robot.rightDrive.getVoltageOutput();

		return new CheesyDriveSystem(left, right, controls.getThrottle(), controls.getWheel(), controls.getQuickTurn());
	}
}
