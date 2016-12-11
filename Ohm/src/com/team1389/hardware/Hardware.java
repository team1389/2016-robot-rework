package com.team1389.hardware;

import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.watch.CompositeWatchable;

public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	String specificHardwareName;
	PortInstance currentPort;

	public void setSpecificHardwareName(String specificHardwareName) {
		this.specificHardwareName = specificHardwareName;
	}

	public abstract void initHardware(int port);

	public int getPort() {
		try {
			return currentPort.index();
		} catch (NullPointerException e) {
			System.err.println("Hardware Object " + getName() + "not attached to a port yet");
			return -1;
		}
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		if (specificHardwareName != null) {
			return specificHardwareName;
		} else {
			return getHardwareIdentifier() + " " + getPort();
		}
	}

}
