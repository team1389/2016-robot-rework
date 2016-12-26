package com.team1389.hardware;

import java.util.Optional;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.util.OptionalUtil;
import com.team1389.watch.CompositeWatchable;

public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	Optional<String> specificHardwareName;
	protected Optional<T> port;

	public Hardware(T requestedPort, Registry registry) {
		this.port = registry.getPort(requestedPort);
		if (!port.isPresent()) {
			System.out.println("hardware failed to initialize on " + requestedPort);
		}
		port.ifPresent(this::init);
	}

	public void setName(String specificHardwareName) {
		this.specificHardwareName = Optional.of(specificHardwareName);
	}

	public abstract void init(T port);

	public int getPort() {
		return OptionalUtil.<T, Integer>ifPresent(-1, PortInstance::index).apply(port);
	}
	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		String defaultName = getHardwareIdentifier() + " " + getPort();
		return specificHardwareName.orElse(defaultName);
	}

}
