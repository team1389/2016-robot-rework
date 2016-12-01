package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;

public class RangeIn<T extends Value> {
	public Class<T> type;
	protected ScalarInput<T> input;
	protected double max, min;

	public RangeIn(Class<T> type, ScalarInput<T> val, double min, double max) {
		this.input = val;
		this.min = min;
		this.max = max;
		this.type = type;
	}

	public RangeIn(Class<T> type, double min, double max) {
		this(type, () -> {
			return 0.0;
		}, min, max);
	}

	public double get() {
		return input.get();
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	public RangeIn<T> mapToRange(double min, double max) {
		input = ScalarInput.mapToRange(input, this.min, this.max, min, max);
		this.min = min;
		this.max = max;
		return this;
	}

	public RangeIn<T> addChangeListener(Runnable onChange) {
		input = ScalarInput.getListeningInput(input, onChange);
		return this;
	}

	public PercentIn mapToPercentIn() {
		return new PercentIn(this);
	}

	public WatchableRangeIn<T> getWatchable(String name) {
		return new WatchableRangeIn<T>(type, this, name);
	}

	public RangeIn<T> invert() {
		input = ScalarInput.invert(input);
		return this;
	}

	public RangeIn<T> scale(double factor) {
		input = ScalarInput.scale(input, factor);
		max *= factor;
		min *= factor;
		return this;
	}

	public RangeIn<T> getWrapped() {
		input = ScalarInput.getWrapped(input, min(), max());
		return this;
	}

	public RangeIn<T> sumInputs(RangeIn<T> rngIn) {
		input = ScalarInput.sum(input, rngIn.input);
		return this;
	}

	/**
	 * creates a boolean source that returns true when the value of the RangeIn
	 * is within the given range
	 * 
	 * @param rangeMin_inclusive
	 *            the lower limit of the range to compare values to
	 * @param rangeMax_exclusive
	 *            the upper limit of the range to compare values to
	 * @return a boolean source that represents whether the current value of the
	 *         RangeIn is within the range
	 */
	public BooleanSource getWithinRange(double rangeMin_inclusive, double rangeMax_exclusive) {
		return () -> {
			double get = get();
			return get < rangeMax_exclusive && get >= rangeMin_inclusive;
		};
	}

}
