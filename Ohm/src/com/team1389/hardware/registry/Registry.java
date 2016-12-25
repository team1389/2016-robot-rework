package com.team1389.hardware.registry;

import java.util.List;

import com.team1389.hardware.registry.port_types.Analog;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.hardware.registry.port_types.USB;
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
	private ResourceManager<Analog> analogPorts;
	private ResourceManager<PCM> pcmPorts;
	private ResourceManager<PWM> pwmPorts;
	private ResourceManager<CAN> canPorts;
	private ResourceManager<DIO> dioPorts;
	private ResourceManager<USB> usbPorts;

	/**
	 * initialize this registry instance
	 */
	public Registry() {
		watcher = new Watcher();
		pcmPorts = new ResourceManager<>();
		analogPorts = new ResourceManager<>();
		pwmPorts = new ResourceManager<>();
		canPorts = new ResourceManager<>();
		dioPorts = new ResourceManager<>();
		usbPorts = new ResourceManager<>();
	}

	/**
	 * add an object to the registry's watcher <br>
	 * this watcher is only used as a storage object for watchables, and is not displayed unless another watcher gets it from {@link #getHardwareInfo()}
	 * 
	 * @param watchable the object to watch
	 */
	public void registerWatchable(Watchable watchable) {
		watcher.watch(watchable);
	}

	public <R extends PortInstance> boolean isUsed(R r) {
		return getRegister(r).isUsed(r);
	}

	public <R extends PortInstance> boolean claim(R r) {
		return getRegister(r).claim(r);
	}

	@SuppressWarnings("unchecked")
	public <T extends PortInstance> ResourceManager<T> getRegister(T r) {
		switch (r.getPortType()) {
		case PWM:
			return (ResourceManager<T>) pwmPorts;
		case ANALOG:
			return (ResourceManager<T>) analogPorts;
		case CAN:
			return (ResourceManager<T>) canPorts;
		case DIO:
			return (ResourceManager<T>) dioPorts;
		case PCM:
			return (ResourceManager<T>) pcmPorts;
		case USB:
			return (ResourceManager<T>) usbPorts;
		default:
			return null;
		}
	}

	/**
	 * @return a list of all hardware watchables associated with this registry
	 */
	public List<Watchable> getHardwareInfo() {
		return watcher.getWatchables();
	}

	public static Registry getInstance() {
		return instance;
	}
	static Registry instance=new Registry();

}
