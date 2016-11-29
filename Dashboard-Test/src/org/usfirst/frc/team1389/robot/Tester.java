package org.usfirst.frc.team1389.robot;

import com.team1389.commands.CommandScheduler;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {

	public static void main(String[] args) {
		initNetworkTablesAsRobot();
		
		Timer.SetImplementation(new TestTimer());

		
		WatchableRangeOut<Position> wheels = new RangeOut<Position>((double val) -> {
		}, 0, 8192).mapToRange(0,360).getWatchable("drivePosition");
		WatchableRangeIn<Position> posIn = new RangeIn<Position>(Position.class,()->{return 1024;},0,8192).getWatchable("test").mapToRange(0,360);
		Watcher dash = new Watcher();
		dash.watch(wheels,posIn);
		CommandScheduler s = new CommandScheduler();
		while (true) {
			s.update();
			wheels.set(45);
			dash.publish(Watcher.DASHBOARD);
			posIn.get();
		}
	}
	public static void initNetworkTablesAsRobot(){
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
