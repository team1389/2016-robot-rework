package com.team1389.hardware;

import java.util.Optional;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.FlagInfo;


public abstract class Hardware<T extends PortInstance> implements CompositeWatchable {
	Optional<String> specificHardwareName;
	final protected Optional<T> port;

	public Hardware(T requestedPort, Registry registry) {
		this.port = registry.getPort(requestedPort);
		if (!port.isPresent()) {
			failInit();
			System.out.println("hardware failed to initialize on " + requestedPort);
		}
		port.ifPresent(this::init);
		specificHardwareName = Optional.empty();
		registry.registerWatchable(this);
	}

	public void setName(String specificHardwareName) {
		this.specificHardwareName = Optional.of(specificHardwareName);
	}

	public abstract void init(T port);

	public abstract void failInit();

	public int getPort() {
		return port.map(PortInstance::index).orElse(-1);
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		String defaultName = getHardwareIdentifier() + " " + getPort();
		return specificHardwareName.orElse(defaultName);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(new FlagInfo("port fault", () -> !port.isPresent()));
	}
}
