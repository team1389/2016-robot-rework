package com.team1389.hardware.registry;

public class PortTakenException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PortTakenException(String msg) {
		super(msg);
	}
}
