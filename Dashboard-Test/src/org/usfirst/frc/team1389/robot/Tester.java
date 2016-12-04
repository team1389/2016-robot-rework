package org.usfirst.frc.team1389.robot;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.trajectory.TrajectoryGenerator;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import layout.TesterDefaultHardware;

public class Tester {
	static TesterDefaultHardware robot;
	static CommandScheduler scheduler;
	static Watcher dash;
	static SystemManager manager;

	static RangeOut<Position> voltageSetter;

	public static void init() {
		SynchronousPIDController<Percent, Position> control = new SynchronousPIDController<Percent, Position>(.7, 0, 0,
				robot.posIn1.scale(Math.PI * 8 * .0254), robot.voltOut1);
		SynchronousPIDController<Percent, Position> control2 = new SynchronousPIDController<Percent, Position>(.7, 0, 0,
				robot.posIn2.scale(Math.PI * 8 * .0254), robot.voltOut2);
		// new DriveCommands(8,2,2,5).driveMetersCommand(5, control, control2);
		// voltageSetter=control.getSetpointSetter();
		// voltageSetter.set(1);
		TrajectoryFollower follower;
		follower = new TrajectoryFollower();
		TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
		config.dt = .01;
		config.max_acc = 10.0;
		config.max_jerk = 60.0;
		config.max_vel = 15.0;
		Trajectory get=TrajectoryGenerator.generate(config, TrajectoryGenerator.SCurvesStrategy, 0, 0, 100,
				0, 0);
		
		follower.setTrajectory(get);
		follower.configure(.7, 0, .1, 0, 0);
		Command followPath = CommandUtil.createCommand(() -> {
			control.getSetpointSetter().set(follower.calculate(robot.posIn1.scale(Math.PI * 8/12).get()));
			return follower.isFinishedTrajectory();
		});
		scheduler.schedule(CommandUtil.combineSimultaneous(
				/*
				 * new DriveCommands(8, .05, .05, 5).driveMetersCommand(5, control, control2), CommandUtil.combineSequential(new WaitTimeCommand(10),new DriveCommands(8, .05, .05, 5).driveMetersCommand(5, control, control2)),
				 */
				followPath, control.getPIDDoCommand(() -> {
					return false;
				}), control2.getPIDDoCommand(() -> {
					return false;
				})));
		dash.watch(robot.posIn1, robot.voltOut1);
	}

	public static void update() {

	}

	public static void main(String[] args) throws InterruptedException {
		initNetworkTablesAsRobot();
		Timer.SetImplementation(new TestTimer());
		HLUsageReporting.SetImplementation(new Interface() {
			@Override
			public void reportSmartDashboard() {
			}

			@Override
			public void reportScheduler() {
			}

			@Override
			public void reportPIDController(int num) {
			}
		});
		dash = new Watcher();
		manager = new SystemManager();
		scheduler = new CommandScheduler();
		robot = new TesterDefaultHardware();
		init();
		while (true) {
			scheduler.update();
			manager.update();
			dash.publish(Watcher.DASHBOARD);
			update();
			Thread.sleep(50);
		}
	}

	public static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
