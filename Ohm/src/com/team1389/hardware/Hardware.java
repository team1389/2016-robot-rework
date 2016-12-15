package com.team1389.hardware;

import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.watch.CompositeWatchable;

public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	String specificHardwareName;
	int port;

	public void setName(String specificHardwareName) {
		this.specificHardwareName = specificHardwareName;
	}

	public abstract void init(int port);

	public void initHardware(int port) {
		this.port = port;
	}

	public int getPort() {
		try {
			return port;
		} catch (NullPointerException e) {
			System.err.println("Hardware Object " + getHardwareIdentifier() + " not attached to a port yet");
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
