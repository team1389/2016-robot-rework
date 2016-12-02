package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.RangeUtil;

/**
 * This class represents a double input stream and contains a number of static methods for manipulating these streams.
 * @author Josh
 *
 * @param <T> Used to make sure of the type of a stream in certain spots in the code
 */
public interface ScalarInput<T extends Value> {
	
	/**
	 * @return The current value of this stream.
	 */
	public double get();

	/**
	 * Maps a stream proportionally to a new range from a given range. Note that if initially a number is outside of the given range,
	 * it will be outside the new range of the resultant stream too.
	 * @param in The stream to operate on
	 * @param inMin The minimum of the input stream range
	 * @param inMax The maximum of the input stream range
	 * @param outMin The minimum of the output stream range
	 * @param outMax The maximum of the output stream range
	 * @return The new stream mapped to the new range
	 */
	static <T extends Value> ScalarInput<T> mapToRange(ScalarInput<T> in, double inMin, double inMax, double outMin,
			double outMax) {
		return () -> {
			return RangeUtil.map(in.get(), inMin, inMax, outMin, outMax);
		};
	}

	/**
	 * Maps a stream proportionally to a percent. The assumed initial min and max are 1 and -1, so it returns a value mapped
	 * between the outMin and outMax proportional to its location between 1 and -1.
	 * @param in The stream to operate on
	 * @param outMin The minimum of the output stream range
	 * @param outMax The maximum of the output stream range
	 * @return The new stream mapped a a percent between outmin and outmax. Now of type Percent
	 */
	static <T extends Value> ScalarInput<Percent> mapToPercent(ScalarInput<?> in, double outMin, double outMax) {
		return () -> {
			return RangeUtil.map(in.get(), -1, 1, outMin, outMax);
		};
		
	}

	/**
	 * 
	 * @param in The stream to operate on
	 * @return The negated stream
	 */
	static <T extends Value> ScalarInput<T> invert(ScalarInput<T> in) {
		return () -> {
			return (-in.get());
		};
	}

	/**
	 * Applies a deadband, a space around zero so close it should be considered as such, to the stream.
	 * @param in The stream to operate on
	 * @param deadband If the magnitude of this stream is less than the magnitude of the value of this parameter, the stream returns zero
	 * @return The stream with the applied deadband
	 */
	static ScalarInput<Percent> applyDeadband(ScalarInput<Percent> in, double deadband) {
		return () -> {
			return RangeUtil.applyDeadband(in.get(), deadband);
		};
	}

	/**
	 * Caps the stream at the limit. If the stream is outside the boundry formed by the limit and its negative, then it gets
	 * set to the limit on whichever side of 0 it is (so either -limit or +limit).
	 * @param in The stream to operate on
	 * @param limit The value of the limit
	 * @return The stream capped by the limit
	 */
	static <T extends Value> ScalarInput<T> limitRange(ScalarInput<T> in, double limit) {
		return () -> {
			return (RangeUtil.limit(in.get(), limit));
		};
	}

	/**
	 * Listens for a change
	 * @param in The stream to operate on
	 * @param onChange What to run when a change happens
	 * @return The resultant stream with the change listener applied. It still does return the current value.
	 */
	static <T extends Value> ScalarInput<T> getListeningInput(ScalarInput<T> in, Runnable onChange) {
		return new ListeningScalarInput<T>(in, onChange);
	}

	/**
	 * 
	 * @param input The stream to operate on
	 * @param factor The number to multiply the stream by
	 * @return A stream with the input stream multiplied by the factor.
	 */
	static <T extends Value> ScalarInput<T> scale(ScalarInput<T> input, double factor) {
		return () -> {
			return input.get() * factor;
		};
	}

	/**
	 * Wraps a number down to between min and max.
	 * @param input Stream to operate on
	 * @param min The mimimum of the new range
	 * @param max The maximum of the new range
	 * @return A stream that is the modulo of the distance between max and min added to the min.
	 */
	static <T extends Value> ScalarInput<T> getWrapped(ScalarInput<T> input, double min, double max) {
		return () -> {
			double val = input.get();
			double divider = (max - min);
			return min + ((val - min) % (divider) + divider) % divider;
		};
	}

	/**
	 * 
	 * @param input Stream 1 to operate on
	 * @param input2 Stream 2 to operate on
	 * @return The sum of input streams one and two
	 */
	static <T extends Value> ScalarInput<T> sum(ScalarInput<T> input, ScalarInput<T> input2) {
		return () -> {
			return input.get() + input2.get();
		};
	}

}
