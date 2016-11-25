package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public interface PercentIn {
	/**
	 * @return a value from -1 to 1
	 */
	public double get();

	public static PercentIn applyDeadband(PercentIn in, double deadband) {
		return () -> {
			return RangeUtil.applyDeadband(in.get(), deadband);
		};
	}

	public static PercentIn limitRange(PercentIn in, double limit) {
		return () -> {
			return RangeUtil.limit(in.get(), limit);
		};
	}

	public static PercentIn mapToPercentRange(RangeIn in) {
		return () -> {
			return RangeUtil.map(in.get(), in.min(), in.max(), -1, 1);
		};
	}
	public static PercentIn invert(PercentIn in){
		return () -> {
			return -in.get();
		};
	}
}
