package com.team1389.hardware;

import java.util.Optional;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.watch.CompositeWatchable;

public abstract class DoubleHardware<T extends PortInstance> implements CompositeWatchable {
	private Optional<T> port1, port2;
	private Optional<String> specificHardwareName;

	public DoubleHardware(T requestedPort1, T requestedPort2, Registry registry) {
		this.port1 = registry.getPort(requestedPort1);
		this.port2 = registry.getPort(requestedPort2);
		if (port1.isPresent() && port2.isPresent()) {
			init(port1.get(), port2.get());
		} else {
			failInit();
			System.out.println("hardware failed to initialize double hardware.");
			if (!port1.isPresent())
				System.out.println(requestedPort1 + " is taken");
			if (!port2.isPresent())
				System.out.println(requestedPort2 + " is taken");
		}
	}

	public abstract void init(T port1, T port2);

	public abstract void failInit();

	public void setName(String specificHardwareName) {
		this.specificHardwareName = Optional.of(specificHardwareName);
	}

	public int getPortA() {
		return port1.map(PortInstance::index).orElse(-1);
	}

	public int getPortB() {
		return port2.map(PortInstance::index).orElse(-1);
	}

	protected abstract String getHardwareIdentifier();

	@Override
	public String getName() {
		String defaultName = getHardwareIdentifier() + " " + getPortA() + " " + getPortB();
		return specificHardwareName.orElse(defaultName);
	}

}
