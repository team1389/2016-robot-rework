package com.team1389.hardware.control;

import com.team1389.hardware.configuration.PIDConstants;

/**
 * A {@link #PIDConfiguration} contains all of the necessary configuration
 * to make PID control work.
 * 
 * All classes that need to deal with PID should take one of these as a argument.
 * @author Jacob Prinz
 */
public class PIDConfiguration {
	public final PIDConstants pidConstants;
	public final boolean isSensorReversed;
	public final boolean isContinuous;
	
	/**
	 * @param pidConstants PID values
	 * @param isSensorReversed which way does the sensor go compared to which way the motor goes?
	 * @param isContinuous is the motion a rotation that can be continuously repeated?
	 */
	public PIDConfiguration(PIDConstants pidConstants, boolean isSensorReversed, boolean isContinuous) {
		this.pidConstants = pidConstants;
		this.isSensorReversed = isSensorReversed;
		this.isContinuous = isContinuous;
	}

}
