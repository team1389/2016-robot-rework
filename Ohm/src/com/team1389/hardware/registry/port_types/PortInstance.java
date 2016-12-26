package com.team1389.hardware.registry.port_types;

public abstract class PortInstance {
	private int index;

	public int index() {
		return index;
	}

	public PortInstance(int port) {
		this.index = port;
	}

	public enum PortType {
		PWM, PCM, CAN, ANALOG, DIO, USB
	}

	public abstract PortType getPortType();

	@Override
	public String toString() {
		return getPortType().name() + " port " + index;
	}
}
