package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.layout.IOHardware;

import com.team1389.watch.Watcher;

public abstract class Operator {
	IOHardware robot;
	Watcher debuggingPanel;
	public Operator(IOHardware robot){
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
