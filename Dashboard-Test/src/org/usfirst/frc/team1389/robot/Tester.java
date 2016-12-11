package org.usfirst.frc.team1389.robot;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.control.MotionProfileController;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.motion_profile.ProfileUtil;
import com.team1389.system.CheesyDriveSystem;
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

	public static void init() {

		MotionProfileController control = new MotionProfileController(.7, 0, 0, robot.posIn1, robot.speedIn1,
				robot.voltOut1);
		control.followProfile(ProfileUtil.generate(-20, 0, .06, .06, 8));
		dash.watch(robot.posIn1.getWatchable("hi"), robot.voltOut1.getWatchable("volts"));
		scheduler.schedule(control.getPIDDoCommand());

		CheesyDriveSystem system = new CheesyDriveSystem(robot.voltOut1, robot.voltOut2, new PercentIn(() -> {
			return -.05;
		}), new PercentIn(() -> {
			return -.05;
		}), new DigitalIn(() -> {
			return false;
		}));
		dash.watch(system);
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
			update();
			scheduler.update();
			manager.update();
			dash.publish(Watcher.DASHBOARD);
			Thread.sleep(50);
		}
	}

	public static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
