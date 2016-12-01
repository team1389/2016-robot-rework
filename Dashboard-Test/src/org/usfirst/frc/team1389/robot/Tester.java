package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.systems.TurretSystem;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.software.LatchedDigitalInput;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.system.System;
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
	static double pos=45;
	static double vltg;

	public static void init() {
		WatchableRangeIn<Angle> turretAngle = new RangeIn<Angle>(Angle.class, () -> {
			return pos;
		}, 0, 360).getWatchable("turret Angle");
		PercentOut voltRange = new PercentOut((double val) -> {
			vltg = val;
		});
		dash.watch(voltRange.getWatchable("turret PWR"));
		PercentIn joy=new PercentIn(()->{return 0;});
		LatchedDigitalInput in=(LatchedDigitalInput) DigitalInput.createInput(()->{return true;}, InputStyle.LATCHED);
		System turret=new TurretSystem(voltRange, turretAngle,joy, in);
		dash.watch(turretAngle);
		manager.register(turret);
	}

	public static void update() {
		pos += 3 * vltg - .5 * (2 * Math.random() - 1);
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
		manager=new SystemManager();
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
