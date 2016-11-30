package com.team1389.util;

public class RangeUtil {
	/**
	 * Re-maps a number from one range to another. 
	 * @param x the value in the original range, Does not constrain values to within the range, because out-of-range values are sometimes intended and useful.
	 * @param in_min the lower limit of the original range
	 * @param in_max the upper limit of the original range
	 * @param out_min the lower limit of the mapped range
	 * @param out_max the upper limit of the mapped range
	 * @return the mapped version of {@code x}
	 */
	public static double map(double x,double in_min,double in_max,double out_min,double out_max){
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * Constrains a value to be between {@code limit} and {@code -limit}
	 * @param v the original value
	 * @param limit the maximum magnitude of {@code v }; if {@code |v|} is greater, it will be truncated.
	 * @return the constrained version of {@code v}
	 */
	public static double limit(double v,double limit){
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
	}
	/**
	 * Truncates any val close to zero, creating a "dead zone" around zero.
	 * @param val the value to test; if it is in the dead zone, it will be set to zero.
	 * @param deadband the minimum distance from zero to avoid truncating
	 * @return the truncated version of {@code val}
	 */
	public static double applyDeadband(double val, double deadband){
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
	}
	
}
