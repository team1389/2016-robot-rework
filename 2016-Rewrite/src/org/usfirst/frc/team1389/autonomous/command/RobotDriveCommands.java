package org.usfirst.frc.team1389.autonomous.command;

import org.usfirst.frc.team1389.robot.RobotConstants;
import org.usfirst.frc.team1389.robot.RobotHardware;

import com.team1389.auto.DriveCommands;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConfiguration;
import com.team1389.configuration.PIDConstants;

public class RobotDriveCommands extends DriveCommands {

	RobotHardware robot;
	public RobotDriveCommands() {
		super(RobotConstants.kWheelDiameter, RobotConstants.kMaxAcceleration, RobotConstants.kMaxVelocity);
		robot=RobotHardware.getInstance();
	}

	public Command driveMetersTalonCommand(double meters){
		return driveMetersCommand(meters,robot.leftDrive.getPositionOutput(new PIDConfiguration(new PIDConstants(0.03,0,0),true,false)),robot.rightDrive.getPositionOutput(new PIDConfiguration(new PIDConstants(0.03,0,0),true,false)),robot.leftDrive.getLeader().getPositionInput(),robot.rightDrive.getLeader().getPositionInput());
	}}
