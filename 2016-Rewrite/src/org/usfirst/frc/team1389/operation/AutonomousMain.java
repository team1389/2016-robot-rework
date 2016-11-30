package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.IOHardware;

import com.team1389.commands.CommandScheduler;
import com.team1389.commands.CommandUtil;
import com.team1389.commands.FollowProfileCommand;
import com.team1389.commands.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;
import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.hardware.valueTypes.Speed;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

public class AutonomousMain extends Operator {
	CommandScheduler autonomousScheduler;

	public AutonomousMain(IOHardware robot) {
		super(robot);
	}

	@Override
	public void disabled() {

	}
	WatchableRangeIn<Position> rightPos;
	WatchableRangeOut<Position> right;
	WatchableRangeIn<Speed> rightSpeed;
	@Override
	protected void init() {
		autonomousScheduler = new CommandScheduler();
		autonomousScheduler.cancelAll();
		PIDConfiguration configleft = new PIDConfiguration(new PIDConstants(0.3, 0.0, 0.0), true, false);
		PIDConfiguration configright = new PIDConfiguration(new PIDConstants(0.3, 0.0, 0.0), true, false);
		
		right = robot.rightDrive.getPositionOutput(configright).invert().mapToRange(0, 1).scale(Math.PI * 8 * 0.0254).getWatchable("rightVal");
		rightPos=robot.rightDrive.getLeader().getPositionInput().mapToRange(0,1).scale(Math.PI*8*.0254).getWatchable("rightPos");
		rightSpeed=robot.rightDrive.getLeader().getSpeedInput().getWatchable("rightSpeed");
		//Command go = new DriveCommands(8, configleft, .5, 1).driveMetersCommand(5, left,right,leftIn,rigthIn);
		Command go2=new FollowProfileCommand(new TrapezoidalMotionProfile(10, .5, .5, 1), right);

		/*
		 * turnAngleCommand(90, 2, robot.navX.getAngleInput(),
		 * robot.leftDrive.getSpeedOutput(config),
		 * robot.rightDrive.getSpeedOutput(config));
		 */

		autonomousScheduler.schedule(go2);
		debuggingPanel.watch(autonomousScheduler, rightPos,rightSpeed,right);
	}

	@Override
	protected void periodic() {
		rightSpeed.get();
		rightPos.get();
		autonomousScheduler.update();
		if(autonomousScheduler.isFinished()){
			//autonomousScheduler.schedule(new FollowProfileCommand(new TrapezoidalMotionProfile(5, .5, .5, 1), right,rightPos.get()));
		}
	}
}
