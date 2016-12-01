package org.usfirst.frc.team1389.robot;

import com.team1389.commands.CommandScheduler;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Position;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {
	static CommandScheduler scheduler;
	static Watcher dash;

	public static void init() {
		System.out.println(new RangeIn<Position>(Position.class,()->{return 4096;},0,8192).mapToRange(0,1).get()*8*Math.PI*.0254);
	}

	public static void update() {
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
		scheduler = new CommandScheduler();
		init();
		while (!scheduler.isFinished()) {
			scheduler.update();
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
