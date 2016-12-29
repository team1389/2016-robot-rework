package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.robot.SimMotor.Motor;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.control.MotionProfileController;
import com.team1389.motion_profile.ProfileUtil;
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
	static MotionProfileController cont;

	public static void init() {
		SimMotor sim = new SimMotor(Motor.CIM);
		cont = new MotionProfileController(.03, 0, 0,
				sim.getPositionInput().mapToRange(0, 1).mapToRange(0, .66 * Math.PI),
				sim.getSpeedInput().mapToRange(0, 1).mapToRange(0, .66 * Math.PI), sim.getVoltageOutput());
		cont.followProfile(ProfileUtil.generate2(5, -3, 1, .1, 6));
		dash.watch(sim.getPositionInput().mapToRange(0, 1).mapToRange(0, .66 * Math.PI).getWatchable("pos"));
		dash.watch(sim.getSpeedInput().mapToRange(0, 1).mapToRange(0, .66 * Math.PI).getWatchable("speed"));
	}

	public static void update() {
		cont.update();
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
