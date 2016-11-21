package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;

public interface OpenRangeInput {
	public double get();

	public double min();

	public double max();

	public static OpenRangeInput mapToOpenRange(OpenRangeInput in, double min, double max) {
		return new OpenRangeInput() {

			@Override
			public double get() {
				return RangeUtil.map(in.get(), in.min(), in.max(), min, max);
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

	public static OpenRangeInput mapToOpenRange(PercentRangeInput in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}

	public static OpenRangeInput getAsOpenRange(PercentRangeInput in) {
		return new OpenRangeInput() {

			@Override
			public double get() {
				return in.get();
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
	public static OpenRangeInput invert(OpenRangeInput in){
		return new OpenRangeInput() {

			@Override
			public double get() {
				return -in.get();
			}

			@Override
			public double min() {
				// TODO Auto-generated method stub
				return -in.max();
			}

			@Override
			public double max() {
				// TODO Auto-generated method stub
				return -min();
			}
		};
	}

}
