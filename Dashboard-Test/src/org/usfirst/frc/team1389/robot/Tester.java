package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.watchers.DashboardInput;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {
	static CommandScheduler scheduler;
	static Watcher dash;
	static SystemManager manager;

	public static void init() {
		DashboardInput.getInstance().init();
	}

	public static void update() {
		DashboardInput.getInstance().getSelectedAutonMode();
	}

	public static void main(String[] args) throws InterruptedException {
		initNetworkTablesAsRobot();
		Timer.SetImplementation(new TestTimer());
		HLUsageReporting.SetImplementation(new Interface() {

			@Override
			public void reportSmartDashboard() {
				// TODO Auto-generated method stub

			}

			@Override
			public void reportScheduler() {
				// TODO Auto-generated method stub

			}

			@Override
			public void reportPIDController(int num) {
				// TODO Auto-generated method stub

			}
		});
		dash = new Watcher();
		manager = new SystemManager();
		scheduler = new CommandScheduler();
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
