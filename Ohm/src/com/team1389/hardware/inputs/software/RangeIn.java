package com.team1389.hardware.inputs.software;

import java.util.function.Function;
import java.util.function.Supplier;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

public class RangeIn<T extends Value> {
	public Class<T> type;
	protected ScalarInput<T> input;// interface that represents a single method that returns a double
	protected double max, min;
	private Supplier<String> operations;

	public RangeIn(Class<T> type, ScalarInput<T> val, double min, double max) {
		this.input = val;
		this.min = min;
		this.max = max;
		this.type = type;
		this.operations = () -> "" + input.get();
	}

	public RangeIn(Class<T> type, Supplier<Double> val, double min, double max) {
		this(type, val::get, min, max);
	}

	public RangeIn(Class<T> type, double min, double max) {
		this(type, () -> 0.0, min, max);
	}

	public double get() {
		return input.get();
	}

	public ScalarInput<T> getStream() {
		return input;
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	public Watchable getWatchable(String name) {
		return new NumberInfo(name, this.input);
	}

	/**
	 * maps this range to an angle value
	 * 
	 * @return the mapped range
	 * 
	 */
	public AngleIn mapToAngle() {
		return new AngleIn(this);
	}

	public PercentIn mapToPercentIn() {
		return new PercentIn(this);
	}

	@SuppressWarnings("unchecked")
	private <R extends RangeIn<T>> R cast() {
		return (R) this;
	}

	public <R extends RangeIn<T>> R setRange(double min, double max) {
		addOperation(d -> " -> setRange[" + min + "," + max + "] = " + d);
		this.min = min;
		this.max = max;
		return cast();
	}

	public <R extends RangeIn<T>> R mapToRange(double min, double max) {
		input = ScalarInput.mapToRange(input, this.min, this.max, min, max);
		addOperation(d -> "-> mapTo[" + min + "," + max + "] = " + d);
		this.min = min;
		this.max = max;
		return cast();
	}

	public <R extends RangeIn<T>> R addChangeListener(Runnable onChange) {
		input = ScalarInput.getListeningInput(input, onChange);
		return cast();
	}

	public <R extends RangeIn<T>> R applyDeadband(double deadband) {
		input = ScalarInput.applyDeadband(input, deadband);
		addOperation(d -> " -> deadband[" + deadband + "] = " + d);
		return cast();
	}

	public <R extends RangeIn<T>> R invert() {
		input = ScalarInput.invert(input);
		addOperation(d -> " -> invert = " + d);
		return cast();
	}

	public <R extends RangeIn<T>> R scale(double factor) {
		ScalarInput<T> in = input;
		input = ScalarInput.scale(input, factor);
		addOperation(d -> " -> scale[" + in.get() + "*" + factor + "] = " + d);
		max *= factor;
		min *= factor;
		return cast();
	}

	public <R extends RangeIn<T>> R getWrapped() {
		input = ScalarInput.getWrapped(input, min(), max());
		addOperation(d -> " -> wrap[" + min + "," + max + "] = " + d);
		return cast();
	}

	public <R extends RangeIn<T>> R sumInputs(RangeIn<T> rngIn) {
		ScalarInput<T> in = input;
		input = ScalarInput.sum(input, rngIn.input);
		addOperation(d -> " -> sum[" + in.get() + "+" + rngIn.get() + "] = " + d);
		return cast();
	}

	public <R extends RangeIn<T>> R limit() {
		addOperation(d -> " -> limit[" + min + "," + max + "] = " + d);
		input = ScalarInput.limitRange(input, this.min, this.max);
		return cast();
	}

	/**
	 * creates a boolean source that returns true when the value of the RangeIn is within the given range
	 * 
	 * @param rangeMin_inclusive the lower limit of the range to compare values to
	 * @param rangeMax_exclusive the upper limit of the range to compare values to
	 * @return a boolean source that represents whether the current value of the RangeIn is within the range
	 */
	public BinaryInput getWithinRange(double rangeMin_inclusive, double rangeMax_exclusive) {
		return () -> {
			double get = get();
			return get < rangeMax_exclusive && get >= rangeMin_inclusive;
		};
	}

	@Override
	public String toString() {
		return operations.get();
	}

	private void addOperation(Function<Double, String> operation) {
		ScalarInput<T> in = input;
		Supplier<String> oldOperations = operations;
		operations = () -> oldOperations.get().concat(operation.apply(in.get()));
	}
}
