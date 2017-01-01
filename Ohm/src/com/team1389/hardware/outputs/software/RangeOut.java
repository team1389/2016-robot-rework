package com.team1389.hardware.outputs.software;

import java.util.function.Consumer;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

public class RangeOut<T extends Value> implements ScalarInput<T> {
	private double lastVal;

	protected ScalarOutput<T> output;
	protected double min, max;
	/**
	 * 
	 * @param out the stream that is operated upon
	 * @param min value
	 * @param max value
	 */
	public RangeOut(ScalarOutput<T> out, double min, double max) {
		this.output = out;
		this.min = min;
		this.max = max;
	}
	/**
	 * 
	 * @param out consumer converted to output stream to be operated on
	 * @param min value
	 * @param max value
	 */
	public RangeOut(Consumer<Double> out,double min,double max){
		this.min = min;
		this.max = max;
		output=out::accept;
	}
	/**
	 * @return value of stream
	 */
	@Override
	public Double get() {
		return lastVal;
	}
	/**
	 * 
	 * @param val the value being passed down the stream 
	 */
	public void set(double val) {
		lastVal = val;
		output.set(val);
	}
	/**
	 * 
	 * @return min value
	 */
	public double min() {
		return min;
	}
	/**
	 * 
	 * @return max value
	 */
	public double max() {
		return max;
	}
	/**
	 * 
	 * @return range for min/max
	 */
	public double range() {
		return min() - max();
	}
	/**
	 * preserve type of stream
	 * @return this stream as the class it is called from
	 */
	@SuppressWarnings("unchecked")
	private <R extends RangeOut<T>> R cast() {
		return (R) this;
	}
	/**
	 * 
	 * @return PercentOut that maps to min and max of this class
	 */
	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}
	/**
	 * 
	 * @return AngleOut that maps to min/max of this class
	 */
	public AngleOut mapToAngle() {
		return new AngleOut(this);
	}
	/**
	 * 
	 * @param min of stream being operated on 
	 * @param max of stream being operated on
	 * @return new stream mapped to given range
	 */
	public <R extends RangeOut<T>> R mapToRange(double min, double max) {
		this.output = ScalarOutput.mapToRange(output, min, max, this.min, this.max);
		this.min = min;
		this.max = max;
		return cast();
	}
	/**
	 * invert the values in the stream
	 * @return new inverted stream
	 */
	public <R extends RangeOut<T>> R invert() {
		this.output = ScalarOutput.invert(output);
		return cast();
	}
	/**
	 * set output to a ProfiledRangeOut
	 * @param maxChange in position 
	 * @param initialPos Current position
	 * @return new stream that is a <ProfiledRangeOut>
	 */
	public <R extends RangeOut<T>> R getProfiledOut(double maxChange, double initialPos) {
		output = new ProfiledRangeOut<T>(output, min, max, maxChange, initialPos);
		return cast();
	}
	/**
	 * add a change listener to the stream
	 * @param onChange what to run if values change
	 * @return new stream with new change listener
	 */
	public <R extends RangeOut<T>> R addChangeListener(Runnable onChange) {
		output = ScalarOutput.getListeningOutput(output, onChange);
		return cast();
	}
	/**
	 * 
	 * @param name key for the value
	 * @return NumberInfo with arguments({@code name}, this stream)
	 */
	public Watchable getWatchable(String name) {
		return new NumberInfo(name, this);
	}
	/**
	 * adds a stream with same min/max, and passes down the same value 
	 * @param outFollow stream to add as a follower
	 * @return this stream but with an additional follower
	 */
	public <R extends RangeOut<T>> R addFollowers(RangeOut<T> outFollow) {
		ScalarOutput<T> out = this.output;
		output = (double val) -> {
			out.set(val);
			outFollow.mapToRange(min, max).set(val);
		};
		return cast();
	}
	/**
	 * causes the stream to set the value of any value in the deadzone to 0.0 
	 * @param deadband max distance    
	 * @return
	 */
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

	public <R extends RangeOut<T>> R offset(ScalarInput<?> offsetAmt) {
		output = ScalarOutput.offset(output, offsetAmt);
		return cast();
	}

	public <R extends RangeOut<T>> R offset(double amt) {
		return offset(() -> {
			return amt;
		});
	}

	public RangeOut<T> scale(double factor) {
		output = ScalarOutput.scale(output, factor);
		max *= factor;
		min *= factor;
		return this;
	}
	public RangeOut<T> setRange(int min, int max) {
		this.min=min;
		this.max=max;
		return this;
	}
}
