package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.IOHardware;

import com.team1389.autonomous.DriveCommands;
import com.team1389.commands.Command;
import com.team1389.commands.CommandScheduler;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;
import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Position;

public class AutonomousMain extends Operator {
	CommandScheduler autonomousScheduler;

	public AutonomousMain(IOHardware robot) {
		super(robot);
	}

	@Override
	public void disabled() {

	}

	@Override
	protected void init() {
		autonomousScheduler = new CommandScheduler();
		autonomousScheduler.cancelAll();
		PIDConfiguration configleft = new PIDConfiguration(new PIDConstants(0.3, 0.0, 0.0), true, false);
		PIDConfiguration configright = new PIDConfiguration(new PIDConstants(0.3, 0.0, 0.0), true, false);

		RangeOut<Position> left = robot.leftDrive.getPositionOutput(configleft);
		RangeOut<Position> right = robot.rightDrive.getPositionOutput(configright).invert();
		WatchableRangeIn<Position> leftIn=robot.leftDrive.getLeader().getPositionInput().getWatchable("leftPos");
		WatchableRangeIn<Position> rigthIn=robot.rightDrive.getLeader().getPositionInput().getWatchable("rightPos");

		Command go = new DriveCommands(8, configleft, .5, 1).driveMetersCommand(5, left,right,leftIn,rigthIn);
		/*
		 * turnAngleCommand(90, 2, robot.navX.getAngleInput(),
		 * robot.leftDrive.getSpeedOutput(config),
		 * robot.rightDrive.getSpeedOutput(config));
		 */

		autonomousScheduler.schedule(go);
		debuggingPanel.watch(autonomousScheduler, leftIn,rigthIn);
	}

	@Override
	protected void periodic() {
		autonomousScheduler.update();
	}
}
