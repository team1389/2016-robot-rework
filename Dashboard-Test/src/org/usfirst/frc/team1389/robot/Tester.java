package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.systems.ArmSystem;
import org.usfirst.frc.team1389.systems.ArmSystem.ArmLocation;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
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
		ButtonEnumMap<ArmLocation> testButtons = new ButtonEnumMap<>(ArmLocation.DEFENSE);
		ArmSystem system = new ArmSystem(robot.voltOut1, testButtons, robot.posIn1.mapToAngle());
		dash.watch(system);
		manager.register(system);
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
