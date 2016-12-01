package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.robot.RobotHardware;

import com.team1389.watch.Watcher;

public abstract class Operator {
	RobotHardware robot;
	Watcher debuggingPanel;
	public Operator(RobotHardware robot){
		this.robot=robot;
		debuggingPanel=new Watcher();
	}
	public void initOperation(){
		init();
	}
	public void periodicOperation(){
		debuggingPanel.publish(Watcher.DASHBOARD);
		periodic();
	}
	public void disabledOperation(){
		debuggingPanel.publish(Watcher.DASHBOARD);
		disabled();
	}
	protected abstract void disabled();
	protected abstract void init();
	protected abstract void periodic();
}
