package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.RangeUtil;

public interface ScalarInput<T extends Value> {
	public double get();

	static <T extends Value> ScalarInput<T> mapToRange(ScalarInput<T> in, double inMin, double inMax, double outMin,
			double outMax) {
		return () -> {
			return RangeUtil.map(in.get(), inMin, inMax, outMin, outMax);
		};
	}

	static <T extends Value> ScalarInput<Percent> mapToPercent(ScalarInput<?> in, double outMin, double outMax) {
		return () -> {
			return RangeUtil.map(in.get(), -1, 1, outMin, outMax);
		};
	}

	static <T extends Value> ScalarInput<T> invert(ScalarInput<T> in) {
		return () -> {
			return (-in.get());
		};
	}

	static ScalarInput<Percent> applyDeadband(ScalarInput<Percent> in, double deadband) {
		return () -> {
			return RangeUtil.applyDeadband(in.get(), deadband);
		};
	}

	static <T extends Value> ScalarInput<T> limitRange(ScalarInput<T> in, double limit) {
		return () -> {
			return (RangeUtil.limit(in.get(), limit));
		};
	}
	
	static <T extends Value> ScalarInput<T> getListeningInput(ScalarInput<T> in, Runnable onChange){
		return new ListeningScalarInput<T>(in,onChange);
	}

	public static <T extends Value> ScalarInput<T> scale(ScalarInput<T> input, double factor) {
		return () -> {
			return input.get()* factor;
		};
	}
}
