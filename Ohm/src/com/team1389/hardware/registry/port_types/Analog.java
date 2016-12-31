package com.team1389.hardware.registry.port_types;

public class Analog extends PortInstance {

	public Analog(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.ANALOG;
	}

}
