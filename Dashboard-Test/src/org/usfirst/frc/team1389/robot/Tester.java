package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.CheesyDriveSystem;
import com.team1389.system.System;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Tester {

	public static void main(String[] args) {
		NetworkTable.setIPAddress("127.0.0.1");
		NetworkTable.setClientMode();
		System drive=setupDriveSystem();
		Watcher dash=new Watcher();
		dash.watch(drive);
		drive.init();
		while (true) {
			drive.update();
			dash.publish(Watcher.DASHBOARD);
		}
	}
	public static System setupDriveSystem() {
		PercentOut left = new PercentOut((double val)->{});
		PercentOut right = left;
		
		PercentIn throttle = new PercentIn(()->{return .25;}).applyDeadband(.02);
		PercentIn wheel = new PercentIn(()->{return .25;}).applyDeadband(.02);
		DigitalInput quickTurnButton = DigitalInput.createInput(()->{return true;}, InputStyle.RAW);

		return new CheesyDriveSystem(left, right, throttle, wheel, quickTurnButton);
	}

}
