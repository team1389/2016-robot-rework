package org.usfirst.frc.team1389.autonomous.command;

import org.usfirst.frc.team1389.robot.RobotConstants;
import org.usfirst.frc.team1389.robot.RobotHardware;

import com.team1389.auto.DriveCommands;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConfiguration;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;

/**
 * This class stores a set of drive commands for this particular robot's drivetrain
 * 
 * @author amind
 * @see DriveCommands
 */
public class RobotCommands extends DriveCommands {

	RobotHardware robot;

	/**
	 * inits the DriveCommands object
	 */
	public RobotCommands() {
		super(RobotConstants.kWheelDiameter, RobotConstants.kMaxAcceleration, RobotConstants.kMaxDeceleration,
				RobotConstants.kMaxVelocity);
		robot = RobotHardware.getInstance();
	}

	/**
	 * drives the given distance using drivetrain's position controllers
	 * 
	 * @param meters the distance to drive
	 * @return the drive command
	 */
	public Command driveMetersTalonCommand(double meters) {
		return driveMetersCommand(meters,
				robot.leftDrive.getPositionOutput(new PIDConfiguration(new PIDConstants(0.03, 0, 0), false, false)),
				robot.rightDrive.getPositionOutput(new PIDConfiguration(new PIDConstants(0.03, 0, 0), true, false)),
				robot.leftDrive.getLeader().getPositionInput(), robot.rightDrive.getLeader().getPositionInput());
	}

	/**
	 * drives the given distance using drivetrain's voltage controllers and position inputs
	 * 
	 * @param meters the distance to drive
	 * @return the drive command
	 */
	public Command driveMetersCommand(double meters) {
		SynchronousPIDController<Percent, Position> left = new SynchronousPIDController<Percent, Position>(
				new PIDConfiguration(new PIDConstants(0.03, 0, 0), false, false),
				robot.leftDrive.getLeader().getPositionInput(), robot.leftDrive.getVoltageOutput());
		SynchronousPIDController<Percent, Position> right = new SynchronousPIDController<Percent, Position>(
				new PIDConfiguration(new PIDConstants(0.03, 0, 0), true, false),
				robot.rightDrive.getLeader().getPositionInput(), robot.rightDrive.getVoltageOutput());
		return driveMetersCommand(meters, left, right);
	}
}
