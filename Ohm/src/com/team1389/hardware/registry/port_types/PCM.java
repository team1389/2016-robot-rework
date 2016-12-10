package com.team1389.hardware.registry.port_types;

public class PCM extends PortInstance {

	public PCM(int port) {
		super(port);
	}

	@Override
	public PortType getPortType() {
		return PortType.PCM;
	}

}
