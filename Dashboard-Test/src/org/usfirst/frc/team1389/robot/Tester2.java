package org.usfirst.frc.team1389.robot;

import com.team1389.commands.CommandScheduler;
import com.team1389.commands.FollowProfileCommand;
import com.team1389.commands.command_base.Command;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.motion_profile.TrapezoidalMotionProfile;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester2 {
	static CommandScheduler scheduler;
	static Watcher dash;

	static PIDController setter;
	static double pos = 45;
	static double vltg;

	public static void init() {
		TrapezoidalMotionProfile trapezoidalMotionProfile = new TrapezoidalMotionProfile(5,1,1,2);
		scheduler.schedule(new FollowProfileCommand(trapezoidalMotionProfile,new RangeOut<Position>((double val)->{System.out.println(val);},0,1).scale(Math.PI*.0254*8).mapToRange(0,1).scale(Math.PI*.0254*8)));
		System.out.println(trapezoidalMotionProfile.getPosition(trapezoidalMotionProfile.getDuration()));
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
