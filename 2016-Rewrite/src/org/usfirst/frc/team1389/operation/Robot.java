
package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotHardware;
import org.usfirst.frc.team1389.watchers.DashboardInput;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.autonomous.AutoModeExecuter;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotHardware robot;
	TeleopMain teleOperator;
    AutoModeExecuter autoModeExecuter;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		DebugDash.getInstance().clearWatchers();
		robot = RobotHardware.getInstance();
		teleOperator = new TeleopMain(robot);
		autoModeExecuter = new AutoModeExecuter();
	}

	public void autonomousInit() {
        autoModeExecuter.stop();
        autoModeExecuter.setAutoMode(DashboardInput.getInstance().getSelectedAutonMode());
        autoModeExecuter.start();
		DebugDash.getInstance().clearWatchers();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		DebugDash.getInstance().display();
	}

	@Override
	public void disabledPeriodic() {
		DebugDash.getInstance().display();
	}

	@Override
	public void teleopInit() {
        autoModeExecuter.stop();
		DebugDash.getInstance().clearWatchers();
		teleOperator.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		teleOperator.periodic();
		DebugDash.getInstance().display();
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
