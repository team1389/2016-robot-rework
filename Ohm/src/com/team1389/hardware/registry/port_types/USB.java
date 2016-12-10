package com.team1389.hardware.registry.port_types;

public class USB extends PortInstance {

	public USB(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.USB;
	}

}
