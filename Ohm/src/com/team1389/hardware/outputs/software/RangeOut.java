package com.team1389.hardware.outputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

public class RangeOut<T extends Value> implements ScalarInput<T> {
	private double lastVal;

	protected ScalarOutput<T> output;
	protected double min, max;

	public RangeOut(ScalarOutput<T> out, double min, double max) {
		this.output = out;
		this.min = min;
		this.max = max;
	}

	@Override
	public double get() {
		return lastVal;
	}

	public void set(double val) {
		lastVal = val;
		output.set(val);
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	public double range() {
		return min() - max();
	}

	@SuppressWarnings("unchecked")
	private <R extends RangeOut<T>> R cast() {
		return (R) this;
	}

	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}

	public AngleOut mapToAngle() {
		return new AngleOut(this);
	}

	public <R extends RangeOut<T>> R mapToRange(double min, double max) {
		this.output = ScalarOutput.mapToRange(output, min, max, this.min, this.max);
		this.min = min;
		this.max = max;
		return cast();
	}

	public <R extends RangeOut<T>> R invert() {
		this.output = ScalarOutput.invert(output);
		return cast();
	}

	public <R extends RangeOut<T>> R getProfiledOut(double maxChange, double initialPos) {
		output = new ProfiledRangeOut<T>(output, min, max, maxChange, initialPos);
		return cast();
	}

	public <R extends RangeOut<T>> R addChangeListener(Runnable onChange) {
		output = ScalarOutput.getListeningOutput(output, onChange);
		return cast();
	}

	public Watchable getWatchable(String name) {
		return new NumberInfo(name, this);
	}

	public <R extends RangeOut<T>> R addFollowers(RangeOut<T> outFollow) {
		ScalarOutput<T> out = this.output;
		output = (double val) -> {
			out.set(val);
			outFollow.mapToRange(min, max).set(val);
		};
		return cast();
	}

	public <R extends RangeOut<T>> R applyDeadband(double deadband) {
		output = ScalarOutput.applyDeadband(output, deadband);
		return cast();
	}

	public <R extends RangeOut<T>> R capRange() {
		return limit(min, max);
	}

	public <R extends RangeOut<T>> R limit(double abs) {
		return limit(-abs, abs);
	}

	public <R extends RangeOut<T>> R limit(double min, double max) {
		output = ScalarOutput.limit(output, min, max);
		return cast();
	}

	public RangeOut<T> scale(double factor) {
		output = ScalarOutput.scale(output, factor);
		max *= factor;
		min *= factor;
		return this;
	}
}
