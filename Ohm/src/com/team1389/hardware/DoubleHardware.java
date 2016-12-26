package com.team1389.hardware;

import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.watch.CompositeWatchable;

public abstract class DoubleHardware<T extends PortInstance> implements CompositeWatchable {
	private T port1, port2;
	private String specificHardwareName;
	public DoubleHardware(T port1, T port2){
		this.port1=port1;
		this.port2=port2;
	}
	public void setName(String specificHardwareName) {
		this.specificHardwareName = specificHardwareName;
	}

	public int getPortA() {
		return port1.index();
	}

	public int getPortB() {
		return port2.index();
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		if (specificHardwareName != null) {
			return specificHardwareName;
		} else {
			return getHardwareIdentifier() + " " + getPortA()+","+getPortB();
		}
	}

}
