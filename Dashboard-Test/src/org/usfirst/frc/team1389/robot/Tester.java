package org.usfirst.frc.team1389.robot;

import com.team1389.commands.CommandScheduler;
import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.System;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {

	public static void main(String[] args) {
		Timer.SetImplementation(new TestTimer());
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
		System drive = setupDriveSystem();
		WatchableRangeOut<Position> wheels = new RangeOut<Position>((double val) -> {
		}, 0, 8192).mapToRange(0, 1).getWatchable("drivePosition");
		wheels.scale(Math.PI * 8 * 0.0254);

		Watcher dash = new Watcher();
		dash.watch(drive, wheels);
		drive.init();
		CommandScheduler s = new CommandScheduler();
		while (true) {
			s.update();
			dash.publish(Watcher.DASHBOARD);
		}
	}

	public static System setupDriveSystem() {
		PercentOut left = new PercentOut((double val) -> {
		});
		PercentOut right = left;

		PercentIn throttle = new PercentIn(() -> {
			return .25;
		}).applyDeadband(.02);
		PercentIn wheel = new PercentIn(() -> {
			return .25;
		}).applyDeadband(.02);
		DigitalInput quickTurnButton = DigitalInput.createInput(() -> {
			return true;
		}, InputStyle.RAW);

		return new CheesyDriveSystem(left, right, throttle, wheel, quickTurnButton);
	}

}
