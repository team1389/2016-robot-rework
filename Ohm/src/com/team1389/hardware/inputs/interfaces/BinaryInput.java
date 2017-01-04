package com.team1389.hardware.inputs.interfaces;

import java.util.Arrays;
import java.util.function.Supplier;

import com.team1389.util.LatchedBoolean;
import com.team1389.util.ToggleBoolean;

/**
 * represents a stream of boolean values, and various options to manipulate them
 * 
 * @author Kenneth
 *
 */

public interface BinaryInput extends Supplier<Boolean> {
	@Override
	public Boolean get();

	/**
	 * 
	 * @param in the stream to be operated on
	 * @return this stream but with values reversed
	 */
	public static BinaryInput invert(BinaryInput in) {
		return () -> {
			return !in.get();
		};
	}

	/**
	 * 
	 * @param in the stream to be operated on
	 * @return a new stream that only returns true if the current val is different from the last val
	 */
	public static BinaryInput getLatched(BinaryInput in) {
		return LatchedBoolean.latch(in)::get;
	}

	/**
	 * 
	 * @param in the stream to be operated on
	 * @return a
	 */
	public static BinaryInput getToggled(BinaryInput in) {
		return ToggleBoolean.toggle(in)::get;
	}

	/**
	 * combines a list of boolean streams into a single stream that returns true if and only if all of the original streams are true
	 * 
	 * @param inputs the list of streams to combine
	 * @return the combined stream
	 */
	public static BinaryInput combineAND(BinaryInput... inputs) {
		return () -> Arrays.stream(inputs).map(input -> input.get()).allMatch(val -> val == true);
	}

	/**
	 * combines a list of boolean streams into a single stream that returns true if any of the original streams are true
	 * 
	 * @param inputs the list of streams to combine
	 * @return the combined stream
	 */
	public static BinaryInput combineOR(BinaryInput... inputs) {
		return () -> Arrays.stream(inputs).map(input -> input.get()).anyMatch(val -> val == true);
	}

	/**
	 * creates a stream that updates a listener when its value changes
	 * 
	 * @param in the stream to track
	 * @param runner the action to perform when the stream changes
	 * @return a new {@code ListeningBinaryInput} stream
	 */
	public static ListeningBinaryInput getListeningSource(BinaryInput in, Runnable runner) {
		return new ListeningBinaryInput(in, runner);
	}
}
