package com.team1389.configuration;

/**
 * PID control configuration constants
 * 
 * @author Jacob Prinz
 */
public final class PIDConstants {
	/**
	 * the default PID gains
	 */
	public static final PIDConstants DEFAULT = new PIDConstants(0, 0, 0);
	/**
	 * the proportional gain
	 */
	public final double p;
	/**
	 * the integral gain
	 */
	public final double i;
	/**
	 * the derivative gain
	 */
	public final double d;
	
	/**
	 * @param p the proportional gain
	 * @param i the integral gain
	 * @param d the derivative gain
	 */
	public PIDConstants(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
}
