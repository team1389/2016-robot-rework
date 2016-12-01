package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Value;

public class RangeOut<T extends Value> {
	protected ScalarOutput<T> output;
	protected double min, max;

	public RangeOut(ScalarOutput<T> out, double min, double max) {
		this.output = out;
		this.min = min;
		this.max = max;
	}

	public void set(double val) {
		output.set(val);
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}
	public double range(){
		return min()-max();
	}

	public RangeOut<T> mapToRange(double min, double max) {
		this.output = ScalarOutput.mapToRange(output, min, max, this.min, this.max);
		this.min = min;
		this.max = max;
		return this;
	}

	public WatchableRangeOut<T> getWatchable(String name) {
		return new WatchableRangeOut<T>(this, name);
	}

	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}

	public RangeOut<Angle> mapToAngle() {
		return new RangeOut<Angle>(ScalarOutput.mapToAngle(this.output, this.min, this.max), 0, 360);
	}

	public RangeOut<T> invert() {
		this.output = ScalarOutput.invert(output);
		return this;
	}

	public RangeOut<T> getProfiledOut(double maxChange,double initialPos) {
		output = new ProfiledRangeOut<T>(output, min, max, maxChange,initialPos);
		return this;
	}
	public RangeOut<T> addChangeListener(Runnable onChange){
		output=ScalarOutput.getListeningOutput(output, onChange);
		return this;
	}
	public RangeOut<T> addFollowers(RangeOut<T> outFollow) {
		ScalarOutput<T> out = this.output;
		output = (double val) -> {
			out.set(val);
			outFollow.mapToRange(min, max).set(val);
		};
		return this;
	}

	public RangeOut<T> scale(double factor) {
		output = ScalarOutput.scale(output, factor);
		max *= factor;
		min *= factor;
		return this;
	}
}
