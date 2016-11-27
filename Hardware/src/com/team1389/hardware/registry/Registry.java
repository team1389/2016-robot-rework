package com.team1389.hardware.registry;

import java.util.List;

import com.team1389.watch.Watchable;
import com.team1389.watch.Watcher;

public class Registry {
	private Watcher watcher;
	private ResourceManager<Integer> pwmPorts;
	private ResourceManager<Integer> canPorts;
	private ResourceManager<Integer> dioPorts;
	
	public Registry() {
		watcher = new Watcher();
		pwmPorts = new ResourceManager<>();
		canPorts = new ResourceManager<>();
		dioPorts = new ResourceManager<>();
	}
	
	public void registerWatcher(Watchable watchable){
		watcher.watch(watchable);
	}
	
	public void claimPWMPort(int port){
		if (pwmPorts.isUsed(port)){
			throw new PortTakenException("PWM port " + port + " is already being used");
		} else {
			pwmPorts.setUsed(port);
		}
	}
	
	public void claimCANPort(int port){
		if (canPorts.isUsed(port)){
			throw new PortTakenException("CAN port " + port + " is already being used");
		} else {
			canPorts.setUsed(port);
		}
	}

	public void claimDIOPort(int port){
		if (dioPorts.isUsed(port)){
			throw new PortTakenException("DIO port " + port + " is already being used");
		} else {
			dioPorts.setUsed(port);
		}
	}
	
	public List<Watchable> getHardwareInfo(){
		return watcher.getWatchables();
	}
}
