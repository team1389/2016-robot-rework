package com.team1389.hardware.registry.port_types;

import edu.wpi.first.wpilibj.SensorBase;

/**
 * represents a generic port
 * 
 * @author amind
 *
 */
public abstract class PortInstance {
	private int index;

	/**
	 * @return the port number
	 */
	public int index() {
		return index;
	}

	/**
	 * @param port the port number
	 */
	public PortInstance(int port) {
		this.index = port;
	}

	/**
	 * possible types of port
	 * 
	 * @author amind
	 *
	 */
	public enum PortType {
		/**
		 * PWM port on the roborio
		 */
		PWM(SensorBase.kPwmChannels),
		/**
		 * PCM port
		 */
		PCM(SensorBase.kSolenoidChannels),
		/**
		 * port on the CAN bus
		 */
		CAN(Integer.MAX_VALUE),
		/**
		 * Analog port
		 */
		ANALOG(SensorBase.kAnalogInputChannels),
		/**
		 * Digital Output port on the roborio
		 */
		DIO(SensorBase.kDigitalChannels),
		/**
		 * USB port on the roborio
		 */
		USB(2);
		protected final int maxPorts;

		private PortType(int maxPorts) {
			this.maxPorts = maxPorts;
		}
	}

	/**
	 * @return the port type
	 */
	public abstract PortType getPortType();

	@Override
	public String toString() {
		return getPortType().name() + " port " + index;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PortInstance) {
			return ((PortInstance) o).index == this.index;
		}
		return false;
	}

	@Override
	public int hashCode(){
		return index*151750009; // hashCodes of PortInstances whose indexes are only slightly different will be different by magnitudes
	}
}
