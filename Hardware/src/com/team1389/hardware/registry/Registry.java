package com.team1389.hardware.registry;

import java.util.List;

import com.team1389.hardware.watch.Watchable;
import com.team1389.hardware.watch.Watcher;

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
		watcher.newWatchable(watchable);
	}
	
	public <T extends Watchable> T registerPWMHardware(int port, Constructor<PWMPort, T> constructor){
		if (pwmPorts.isUsed(port)){
			throw new PortTakenException("PWM port " + port + " is already being used");
		} else {
			pwmPorts.setUsed(port);
			PWMPort pwm = new PWMPort(port);
			T t = constructor.construct(pwm);
			watcher.newWatchable(t);
			return t;
		}
	}
	
	public <T extends Watchable> T registerCANHardware(int port, Constructor<CANPort, T> constructor){
		if (canPorts.isUsed(port)){
			throw new PortTakenException("CAN port " + port + " is already being used");
		} else {
			canPorts.setUsed(port);
			CANPort can = new CANPort(port);
			T t =  constructor.construct(can);
			watcher.newWatchable(t);
			return t;
		}
	}

	public <T extends Watchable> T registerDIOHardware(int port, Constructor<DIOPort, T> constructor){
		if (dioPorts.isUsed(port)){
			throw new PortTakenException("DIO port " + port + " is already being used");
		} else {
			dioPorts.setUsed(port);
			DIOPort dio = new DIOPort(port);
			T t =  constructor.construct(dio);
			watcher.newWatchable(t);
			return t;
		}
	}
	
	public List<Watchable> getHardwareInfo(){
		return watcher.getWatchables();
	}
}
