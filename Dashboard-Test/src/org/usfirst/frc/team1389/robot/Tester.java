package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.robot.SimMotor.Motor;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.control.SmoothSetController;
import com.team1389.hardware.inputs.hardware.DashboardScalarInput;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
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
	static SmoothSetController cont;
	static DashboardScalarInput inp;
	static RangeOut<Position> setter;
	public static void init() {
		SimMotor sim = new SimMotor(Motor.CIM, 7.5, 0.66, true);
		RangeIn<Position> pos = sim.getPositionInput().mapToRange(0, 1).mapToRange(0, 360);
		RangeIn<Speed> speed = sim.getSpeedInput().mapToRange(0, 1).mapToRange(0, 360);
		dash.watch(pos.getWatchable("pos"), speed.getWatchable("speed"));
		cont = new SmoothSetController(.03, 0, 2, 10, 10, 30, pos, speed, sim.getVoltageOutput());
	//	cont = new SynchronousPIDController<>(.03, 0, 0, pos, sim.getVoltageOutput());
		inp = new DashboardScalarInput("setpoint", Watcher.DASHBOARD, 0.0);
		setter=cont.getSetpointSetter();
		cont.setSetpoint(20);
	}

	public static void update() {
		cont.update();
		setter.set(inp.get());
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
			dash.log(Watcher.FILEWRITER);
			Thread.sleep(50);
		}
	}

	public static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
