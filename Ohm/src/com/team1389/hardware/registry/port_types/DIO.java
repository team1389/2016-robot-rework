package com.team1389.hardware.registry.port_types;

public class DIO extends PortInstance {

	public DIO(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.DIO;
	}

}
