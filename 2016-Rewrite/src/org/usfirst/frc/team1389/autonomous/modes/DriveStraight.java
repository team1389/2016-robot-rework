package org.usfirst.frc.team1389.autonomous.modes;

import org.usfirst.frc.team1389.autonomous.command.RobotDriveCommands;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;

public class DriveStraight extends AutoModeBase{
	private final double kDistanceToDrive = 5;
	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(new RobotDriveCommands().driveMetersTalonCommand(kDistanceToDrive));
	}
	
}
