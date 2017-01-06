package org.usfirst.frc.team1389.robot;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.control.SmoothSetController;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.hardware.DashboardScalarInput;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
//import com.team1389.hardware.value_types.Speed; // Unused
import com.team1389.hardware.value_types.Value;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import layout.TesterDefaultHardware;
import motor_sim.Attachment;
import motor_sim.Motor;
import motor_sim.MotorSystem;
import motor_sim.element.PrismElement;

public class Tester {
	static TesterDefaultHardware robot;
	static CommandScheduler scheduler;
	static Watcher dash;
	static SystemManager manager;
	static SynchronousPIDController<Percent, Position> cont;
	static DashboardScalarInput inp;
	static RangeOut<Position> setter;
	static MotorSystem sim;
	static Watcher dash2;

	public static void init() {
		dash2 = new Watcher();
		sim = new MotorSystem(new Attachment(new PrismElement(7.9, .4, .05, .66), true), 200, Motor.MINI_CIM,
				Motor.MINI_CIM);
		sim.setRangeOfMotion(0, 89);
		RangeIn<Position> pos = sim.getPositionInput().mapToRange(0, 360);
		RangeIn<Speed> speed = sim.getSpeedInput().mapToRange(0, 1).scale(60);
		cont = new SmoothSetController(.07, 0, 5, 10, 10, 30, pos, speed, sim.getVoltageOutput());
		cont.setInputRange(0, 90);
		dash2.watch(speed.getWatchable("speed"),pos.getWatchable("pos"));
		dash2.watch(cont.getPIDTuner("jo"));
	}


	public static void update() {
		dash2.publish(Watcher.DASHBOARD);
		cont.update();
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
		// dash2.setLogLocation(LogFile.make(LogFile.dateTimeFormatter().concat("Dashboard_"), "logs/", LogType.CSV));

		while (true) {
			timer.zero();
			update();
			scheduler.update();
			manager.update();
			dash.publish(Watcher.DASHBOARD);
			// dash2.log();
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
