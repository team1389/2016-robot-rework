package com.team1389.hardware;

import java.util.Optional;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.watch.CompositeWatchable;

public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	Optional<String> specificHardwareName;
	Optional<T> port;

	public Hardware(T requestedPort, Registry registry) {
		this.port = registry.getPort(requestedPort);
		if (port.isPresent()) {
			init(getPort());
		} else {
			System.out.println("hardware failed to initialize on " + requestedPort);
			// hardware not inited
		}
	}

	public void setName(String specificHardwareName) {
		this.specificHardwareName = Optional.of(specificHardwareName);
	}

	public abstract void init(int port);

	public int getPort() {
		if (port.isPresent()) {
			return port.get().index();
		} else {
			return -1;
		}
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		String defaultName = getHardwareIdentifier() + " " + getPort();
		return specificHardwareName.orElse(defaultName);
	}

}
