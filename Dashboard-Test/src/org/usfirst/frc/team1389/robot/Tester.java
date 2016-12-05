package org.usfirst.frc.team1389.robot;

import com.team1389.auto.DriveCommands;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.CommandUtil;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;

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
				robot.posIn1, robot.voltOut1);
		SynchronousPIDController<Percent, Position> control2 = new SynchronousPIDController<Percent, Position>(.7, 0, 0,
				robot.posIn2, robot.voltOut2);
		scheduler.schedule(CommandUtil.combineSimultaneous(

				new DriveCommands(8, .06, .06, 2).driveMetersCommand(20, control, control2, robot.speedIn1,
						robot.speedIn2),
				CommandUtil.combineSequential(new WaitTimeCommand(10), CommandUtil.createCommand(() -> {
					scheduler.cancelAll();
					scheduler.schedule(new DriveCommands(8, .06, .06, 2).driveMetersCommand(20, control, control2, robot.speedIn1,
						robot.speedIn2));
					return true;
				})),

				/*
				 * followPath, control.getPIDDoCommand(() -> { return false; }),
				 */ control.getPIDDoCommand(() -> {
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
