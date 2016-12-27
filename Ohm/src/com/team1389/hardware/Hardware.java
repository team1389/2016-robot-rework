package com.team1389.hardware;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.util.Optional;
import com.team1389.watch.CompositeWatchable;

public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	Optional<String> specificHardwareName;
	protected Optional<T> port;

	public Hardware(T requestedPort, Registry registry) {
		this.port = registry.getPort(requestedPort);
		if (!port.isPresent()) {
			failInit();
			System.out.println("hardware failed to initialize on " + requestedPort);
		}
		port.ifPresent(this::init);
	}

	public void setName(String specificHardwareName) {
		this.specificHardwareName = Optional.of(specificHardwareName);
	}

	public abstract void init(T port);

	public abstract void failInit();

	public int getPort() {
		return port.ifPresent(-1, PortInstance::index).get();
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		String defaultName = getHardwareIdentifier() + " " + getPort();
		return specificHardwareName.orElse(defaultName);
	}
	// TODO subwatchables use streams, make port fault a watchable of all hardware
}
