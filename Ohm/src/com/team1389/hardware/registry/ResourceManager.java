package com.team1389.hardware.registry;

import java.util.ArrayList;
import java.util.List;

import com.team1389.hardware.registry.port_types.PortInstance;

public class ResourceManager<T extends PortInstance> {

	private List<T> usedPorts;

	public ResourceManager() {
		usedPorts = new ArrayList<>();
	}

	public boolean isUsed(T t) {
		return usedPorts.contains(t);
	}

	public boolean claim(T t) {
		return usedPorts.add(t);
	}

}
