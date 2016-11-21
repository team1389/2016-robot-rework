package com.team1389.hardware.configuration;

/**
 * PID control configuration constants 
 * @author Jacob Prinz
 */
public class PIDConstants {
	public final double p, i, d;
	public PIDConstants(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
}
