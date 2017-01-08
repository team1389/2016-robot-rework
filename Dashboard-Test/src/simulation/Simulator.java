package simulation;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.util.Loopable;
import com.team1389.util.Timer;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Simulator {
	CommandScheduler scheduler;
	static Timer timer;
	Loopable loopable;

	/**
	 * hangs thread while simulating
	 * 
	 * @throws InterruptedException
	 *             if the simulation is interrupted
	 */
	public static void simulate(Loopable loopable) throws InterruptedException {
		initWPILib();
		loopable.init();
		while (true) {
			timer.zero();
			loopable.update();
			if (timer.get() < .05) {
				Thread.sleep((long) (50 - 1000 * timer.get()));
			}
		}
	}

	public static void initWPILib() {
		edu.wpi.first.wpilibj.Timer.SetImplementation(new SimulationTimer());
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
	}

	public static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
