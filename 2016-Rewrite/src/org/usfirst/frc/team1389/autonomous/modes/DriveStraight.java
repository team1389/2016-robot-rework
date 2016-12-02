package org.usfirst.frc.team1389.autonomous.modes;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
/**
 * This auto mode drives straight for {@link #kDistanceToDrive} meters
 * @author amind
 *
 */
public class DriveStraight extends AutoModeBase{
	private final double kDistanceToDrive = 5;
	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(new RobotCommands().driveMetersCommand(kDistanceToDrive));
	}
	
}
