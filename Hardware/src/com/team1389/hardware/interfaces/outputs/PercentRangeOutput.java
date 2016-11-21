package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.util.RangeUtil;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public interface PercentRangeOutput {
	/**
	 * @return a value from -1 to 1
	 */
	public void set(double val);

	public static PercentRangeOutput applyDeadband(PercentRangeOutput in, double deadband) {
		return (double val) -> {
			in.set(RangeUtil.applyDeadband(val, deadband));
		};
	}

	public static PercentRangeOutput limitRange(PercentRangeOutput in, double limit) {
		return (double val) -> {
			in.set(RangeUtil.limit(val, limit));
		};
	}

	public static PercentRangeOutput mapToPercentRange(OpenRangeOutput in) {
		return (double val) -> {
			in.set(RangeUtil.map(val, -1, 1, in.min(), in.max()));
		};
	}
	public static PercentRangeOutput invert(PercentRangeOutput in){
		return (double val) -> {
			in.set(-val);
		};
	}
}
