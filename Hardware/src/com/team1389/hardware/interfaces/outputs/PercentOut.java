package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.util.RangeUtil;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public interface PercentOut {
	/**
	 * @return a value from -1 to 1
	 */
	public void set(double val);

	public static PercentOut applyDeadband(PercentOut in, double deadband) {
		return (double val) -> {
			in.set(RangeUtil.applyDeadband(val, deadband));
		};
	}

	public static PercentOut limitRange(PercentOut in, double limit) {
		return (double val) -> {
			in.set(RangeUtil.limit(val, limit));
		};
	}

	public static PercentOut mapToPercentRange(RangeOut in) {
		return (double val) -> {
			in.set(RangeUtil.map(val, -1, 1, in.min(), in.max()));
		};
	}
	public static PercentOut invert(PercentOut in){
		return (double val) -> {
			in.set(-val);
		};
	}
}
