package com.team1389.hardware.registry;

import java.util.List;

import com.team1389.watch.Watchable;
import com.team1389.watch.Watcher;

/**
 * This class tracks all hardware objects and ensures that no hardware attempts to install on a taken port, to avoid crashing the robot code
 * 
 * @author amind
 *
 */
public class Registry {
	private Watcher watcher;
	private ResourceManager<Integer> pwmPorts;
	private ResourceManager<Integer> canPorts;
	private ResourceManager<Integer> dioPorts;

	/**
	 * initialize this registry instance
	 */
	public Registry() {
		watcher = new Watcher();
		pwmPorts = new ResourceManager<>();
		canPorts = new ResourceManager<>();
		dioPorts = new ResourceManager<>();
	}

	/**
	 * add an object to the registry's watcher <br>
	 * this watcher is only used as a storage object for watchables, and is not displayed unless another watcher gets it from {@link #getHardwareInfo()}
	 * 
	 * @param watchable the object to watch
	 */
	public void registerWatcher(Watchable watchable) {
		watcher.watch(watchable);
	}

	/**
	 * registers the given PWM port as taken.
	 * 
	 * @param port the port to claim
	 * @throws PortTakenException if port is already taken
	 */
	public void claimPWMPort(int port) {
		if (pwmPorts.isUsed(port)) {
			throw new PortTakenException("PWM port " + port + " is already being used");
		} else {
			pwmPorts.setUsed(port);
		}
	}

	/**
	 * registers the given CAN port as taken.
	 * 
	 * @param port the port to claim
	 * @throws PortTakenException if port is already taken
	 */
	public void claimCANPort(int port) {
		if (canPorts.isUsed(port)) {
			throw new PortTakenException("CAN port " + port + " is already being used");
		} else {
			canPorts.setUsed(port);
		}
	}

	/**
	 * registers the given DIO port as taken.
	 * 
	 * @param port the port to claim
	 * @throws PortTakenException if port is already taken
	 */
	public void claimDIOPort(int port) {
		if (dioPorts.isUsed(port)) {
			throw new PortTakenException("DIO port " + port + " is already being used");
		} else {
			dioPorts.setUsed(port);
		}
	}

	/**
	 * @return a list of all hardware watchables associated with this registry
	 */
	public List<Watchable> getHardwareInfo() {
		return watcher.getWatchables();
	}
}
