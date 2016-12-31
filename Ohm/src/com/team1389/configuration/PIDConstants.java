package com.team1389.configuration;

/**
 * PID control configuration constants
 * 
 * @author Jacob Prinz
 */
public final class PIDConstants {
	public static final PIDConstants DEFAULT = new PIDConstants(0, 0, 0);

	public final double p, i, d;

	public PIDConstants(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
}
