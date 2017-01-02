package org.usfirst.frc.team1389.robot;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.control.SmoothSetController;
import com.team1389.hardware.inputs.hardware.DashboardScalarInput;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.SystemManager;
import com.team1389.watch.LogFile;
import com.team1389.watch.LogFile.LogType;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import layout.TesterDefaultHardware;
import motor_sim.Attachment;
import motor_sim.Motor;
import motor_sim.SimulatedActuator;

public class Tester {
	static TesterDefaultHardware robot;
	static CommandScheduler scheduler;
	static Watcher dash;
	static SystemManager manager;
	static SmoothSetController cont;
	static DashboardScalarInput inp;
	static RangeOut<Position> setter;
	static SimulatedActuator sim;
	static Watcher dash2;

	public static void init() {
		dash2 = new Watcher();
		sim = new SimulatedActuator(Motor.MINI_CIM, new Attachment(7.5, 0.66, true), 200);
		sim.setRangeOfMotion(0, 90);
		RangeIn<Position> pos = sim.getPositionInput().mapToRange(0, 1).mapToRange(0, 360);
		RangeIn<Speed> speed = sim.getSpeedInput().mapToRange(0, 1).mapToRange(0, 360);
		dash.watch(pos.getWatchable("pos"));
		cont = new SmoothSetController(.07, 0, 5, 10, 10, 30, pos, speed, sim.getVoltageOutput());
		dash2.watch(cont.getPIDTuner("tuner"));
		cont.setSetpoint(-20);
	}

	public static void update() {
		cont.update();
		dash2.publish(Watcher.DASHBOARD);
		sim.update();
	}

	public static void main(String[] args) throws InterruptedException {
		edu.wpi.first.wpilibj.Timer.SetImplementation(new TestTimer());
		initNetworkTablesAsRobot();
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
		timer = new Timer();
		RangeOut<Value> out = new RangeOut<Value>(k -> {
		}, 0, 1);
		dash.setLogLocation(LogFile.make(LogFile.dateTimeFormatter().concat("Dashboard_"), "logs/", LogType.CSV));

		while (true) {
			timer.zero();
			update();
			scheduler.update();
			manager.update();
			dash.publish(Watcher.DASHBOARD);
			dash.log();
			out.set(timer.get());
			if (timer.get() < .05) {
				Thread.sleep((long) (50 - 1000 * timer.get()));
			}
		}
	}

	static Timer timer;

	public static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
