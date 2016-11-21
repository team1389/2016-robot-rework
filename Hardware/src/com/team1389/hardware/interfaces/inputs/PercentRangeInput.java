package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public interface PercentRangeInput {
	/**
	 * @return a value from -1 to 1
	 */
	public double get();

	public static PercentRangeInput applyDeadband(PercentRangeInput in, double deadband) {
		return () -> {
			return RangeUtil.applyDeadband(in.get(), deadband);
		};
	}

	public static PercentRangeInput limitRange(PercentRangeInput in, double limit) {
		return () -> {
			return RangeUtil.limit(in.get(), limit);
		};
	}

	public static PercentRangeInput mapToPercentRange(OpenRangeInput in) {
		return () -> {
			return RangeUtil.map(in.get(), in.min(), in.max(), -1, 1);
		};
	}
	public static PercentRangeInput invert(PercentRangeInput in){
		return () -> {
			return -in.get();
		};
	}
}
