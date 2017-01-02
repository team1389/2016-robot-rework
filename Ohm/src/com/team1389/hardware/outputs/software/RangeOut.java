package com.team1389.hardware.outputs.software;

import java.util.function.Consumer;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;
/**
 * a stream of doubles with a range, and methods to deal with conversion and range of the stream
 * @author Kenneth 
 *
 * @param <T> The type that the doubles in the stream are representing 
 */
public class RangeOut<T extends Value> implements ScalarInput<T> {
	private double lastVal;

	protected ScalarOutput<T> output;
	protected double min, max;
	/**
	 * 
	 * @param out the stream that is operated upon
	 * @param min the min value
	 * @param max the max value
	 */
	public RangeOut(ScalarOutput<T> out, double min, double max) {
		this.output = out;
		this.min = min;
		this.max = max;
	}
	/**
	 * 
	 * @param out consumer converted to output stream to be operated on
	 * @param min the min value
	 * @param max the max value
	 */
	public RangeOut(Consumer<Double> out,double min,double max){
		this.min = min;
		this.max = max;
		output=out::accept;
	}
	/**
	 * @return value the value of the stream
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
	 * @return min the min value 
	 */
	public double min() {
		return min;
	}
	/**
	 * 
	 * @return max the max value
	 */
	public double max() {
		return max;
	}
	/**
	 * 
	 * @return range the value of the range
	 */
	public double range() {
		return min() - max();
	}
	/**
	 * preserves type of stream
	 * @return this stream as the stream it is called from
	 */
	@SuppressWarnings("unchecked")
	private <R extends RangeOut<T>> R cast() {
		return (R) this;
	}
	/**
	 * map this stream from percent to a range of {@code min} to {@code max} 
	 * @return PercentOut the new stream that is this stream mapped to the aforementioned range
	 */
	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}
	/**
	 * maps this stream from angle to a range of {@code min} to {@code max}
	 * @return AngleOut the new stream that is this stream mapped to the aforementioned range
	 */
	public AngleOut mapToAngle() {
		return new AngleOut(this);
	}
	/**
	 * 
	 * @param min the desired min value
	 * @param max the desired max value
	 * @return this stream mapped to given range
	 */
	public <R extends RangeOut<T>> R mapToRange(double min, double max) {
		this.output = ScalarOutput.mapToRange(output, min, max, this.min, this.max);
		this.min = min;
		this.max = max;
		return cast();
	}
	/**
	 * inverts the values in the stream
	 * @return new inverted stream
	 */
	public <R extends RangeOut<T>> R invert() {
		this.output = ScalarOutput.invert(output);
		return cast();
	}
	/**
	 * sets output to a ProfiledRangeOut
	 * @param maxChange the max change in position 
	 * @param initialPos the Current position
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
	 * @param name the key that corresponds with the stream
	 * @return NumberInfo with arguments({@code name}, this stream)
	 */
	public Watchable getWatchable(String name) {
		return new NumberInfo(name, this);
	}
	/**
	 * adds a stream with same min/max, and passes down the same value 
	 * @param outFollow stream to add as a follower
	 * @return new stream with an additional follower
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
	 * @param deadband the max distance from 0 (deadzone)   
	 * @return new stream with Deadband of {@code deadband}
	 */
	public <R extends RangeOut<T>> R applyDeadband(double deadband) {
		output = ScalarOutput.applyDeadband(output, deadband);
		return cast();
	}
	/**
	 * constrains values in the stream to be between min and max of this stream
	 * @return new stream that is limited to a range 
	 */
	public <R extends RangeOut<T>> R capRange() {
		return limit(min, max);
	}
	/**
	 * 
	 * @param abs the absolute value of min/max of range
	 * @return new stream that is limited to a range
	 */
	public <R extends RangeOut<T>> R limit(double abs) {
		return limit(-abs, abs);
	}
	/**
	 * constrains values in the stream to be between min and max arguments
	 * @param min the min value of desired capped range
	 * @param max the max value of desired capped range
	 * @return new stream that is limited to a range
	 */
	public <R extends RangeOut<T>> R limit(double min, double max) {
		output = ScalarOutput.limit(output, min, max);
		return cast();
	}
	/**
	 * 
	 * @param offsetAmt the stream that holds the values which this stream is offset by
	 * @return this stream but with all the values offset by the value in the input stream
	 */
	public <R extends RangeOut<T>> R offset(ScalarInput<?> offsetAmt) {
		output = ScalarOutput.offset(output, offsetAmt);
		return cast();
	}
	/**
	 * 
	 * @param amt the value that is added to every value in this stream
	 * @return this stream but with {@code amt} added to every value
	 */
	public <R extends RangeOut<T>> R offset(double amt) {
		return offset(() -> {
			return amt;
		});
	}
	
	/**
	 * 
	 * @param factor value to scale by
	 * @return this stream but with min, max, and val multiplied by {@code factor}
	 */
	public RangeOut<T> scale(double factor) {
		output = ScalarOutput.scale(output, factor);
		max *= factor;
		min *= factor;
		return this;
	}
	/**
	 * 
	 * @param min value to set as min
	 * @param max value to set as max
	 * @return this stream but with a new min and max
	 */
	public RangeOut<T> setRange(int min, int max) {
		this.min=min;
		this.max=max;
		return this;
	}
}
