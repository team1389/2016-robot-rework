package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.util.RangeUtil;

public interface OpenRangeOutput {
	public void set(double val);

	public double min();

	public double max();

	public static OpenRangeOutput mapToOpenRange(OpenRangeOutput in, double min, double max) {
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(RangeUtil.map(val, min, max, in.min(), in.max()));
			}

			@Override
			public double min() {
				return min;
			}

			@Override
			public double max() {
				return max;
			}

		};
	}

	public static OpenRangeOutput mapToOpenRange(PercentRangeOutput in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}

	public static OpenRangeOutput getAsOpenRange(PercentRangeOutput in) {
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(val);
			}

			@Override
			public double min() {
				return -1;
			}

			@Override
			public double max() {
				return 1;
			}

		};
	}
	public static OpenRangeOutput invert(OpenRangeOutput in){
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(-val);
			}

			@Override
			public double min() {
				return -in.max();
			}

			@Override
			public double max() {
				return -min();
			}
		};
	}

}
