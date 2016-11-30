package org.usfirst.frc.team1389.robot;

import com.team1389.commands.CommandScheduler;
import com.team1389.commands.CommandUtil;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDConfiguration;
import com.team1389.control.PIDSystemCreator;
import com.team1389.control.SynchronousPID;
import com.team1389.control.pid_wrappers.input.PIDRangeSource;
import com.team1389.control.pid_wrappers.output.PIDControlledRange;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.valueTypes.Angle;
import com.team1389.hardware.valueTypes.Percent;
import com.team1389.hardware.valueTypes.Value;
import com.team1389.util.FIFO;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {
	static CommandScheduler scheduler;
	static Watcher dash;

	static PIDController setter;
	static double pos = 45;
	static double vltg;

	public static void init() {
		WatchableRangeIn<Angle> turretAngle = new RangeIn<Angle>(Angle.class, () -> {
			return pos;
		}, 0, 360).getWatchable("turret Angle");
		WatchableRangeOut<Percent> voltRange = new PercentOut((double val) -> {
			vltg = val;
		}).getWatchable("turret PWR");
		dash.watch(voltRange, turretAngle);
		SynchronousPID setter = new SynchronousPID(.07,0,.15);
		setter.setInputRange(-180, 180);
		setter.setOutputRange(-1, 1);
		WatchableRangeOut<Value> setpointSetter=new RangeOut<Value>((double setPoint)->{setter.setSetpoint(setPoint);},-180,180).getProfiledOut(5,pos).getWatchable("setpoint");
		dash.watch(setpointSetter);
		scheduler.schedule(CommandUtil.createCommand(() -> {
			setpointSetter.set(0);
			voltRange.set(setter.calculate(turretAngle.get()));
			Watcher.DASHBOARD.putNumber("avg error",setter.getAvgError());
			return setter.getSetpoint()==0&&setter.onTargetStable(.5);
		}));

	}

	public static void update() {
		pos += 3*vltg-.5*(2*Math.random()-1);
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
