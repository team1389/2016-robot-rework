package com.team1389.hardware.registry.port_types;

public class PWM extends PortInstance {

	public PWM(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.PWM;
	}

}
