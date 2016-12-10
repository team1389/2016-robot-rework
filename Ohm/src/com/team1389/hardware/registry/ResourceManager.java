package com.team1389.hardware.registry;

import java.util.HashMap;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.registry.port_types.PortInstance;

public class ResourceManager<T extends PortInstance> {

	private HashMap<T, Hardware<T>> registered;

	public ResourceManager() {
		registered = new HashMap<T, Hardware<T>>();
	}

	public boolean isUsed(T t) {
		return registered.containsKey(t);
	}

	public Hardware<T> get(T t) {
		return registered.get(t);
	}

	private void put(T t, Hardware<T> h) {
		registered.put(t, h);
	}

	public Hardware<T> register(T t, Hardware<T> h) {
		if (isUsed(t)) {
			return get(t);
		} else {
			put(t, h);
			return h;
		}
	}
}
