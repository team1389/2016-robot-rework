package com.team1389.hardware.registry.port_types;

public class CAN extends PortInstance {

	public CAN(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.CAN;
	}

}
