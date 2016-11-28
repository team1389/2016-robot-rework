
package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.layout.IOHardware;
import org.usfirst.frc.team1389.operation.TeleopMain;

import com.team1389.autonomous.DriveCommands;
import com.team1389.commands.CommandScheduler;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	IOHardware robot;
	TeleopMain teleoperator;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		robot = new IOHardware();
		teleoperator = new TeleopMain(robot);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	CommandScheduler scheduler;

	public void autonomousInit() {
		scheduler = new CommandScheduler();
		PIDConfiguration config = new PIDConfiguration(new PIDConstants(0.3, 0.0, 0.0), false, false);
		scheduler.schedule(new DriveCommands(8, config).turnAngleCommand(90, 2, robot.navX.getAngleInput(),
				robot.leftDrive.getSpeedOutput(config), robot.rightDrive.getSpeedOutput(config)));
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		scheduler.update();
	}

	@Override
	public void disabledPeriodic() {
		teleoperator.teleopDisabled();
	}

	@Override
	public void teleopInit() {
		teleoperator.teleopInit();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		teleoperator.teleopPeriodic();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {
	}

	public void testPeriodic() {
	}

}
