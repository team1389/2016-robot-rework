package com.team1389.hardware.inputs.interfaces;
import java.util.function.Supplier;


/**
 * represents a stream of boolean values, and various options to manipulate them
 * @author Kenneth
 *
 */

public interface BinaryInput extends Input<Boolean>, Supplier<Boolean>{
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
	public static LatchedBinaryInput getLatched(BinaryInput in) {
		return new LatchedBinaryInput(in);
	}
	/**
	 * 
	 * @param in the stream to be operated on
	 * @return a 
	 */
	public static ToggledBinaryInput getToggled(BinaryInput in) {
		LatchedBinaryInput input;
		if (in instanceof LatchedBinaryInput) {
			input = (LatchedBinaryInput) in;
		} else {
			input = getLatched(in);
		}
		return new ToggledBinaryInput(input);
	}

	public static BinaryInput combineAND(BinaryInput... inputs) {
		return () -> {
			boolean stillTrue = true;
			for (BinaryInput input : inputs) {
				stillTrue = stillTrue && input.get();
			}
			return stillTrue;
		};
	}

	public static BinaryInput combineOR(BinaryInput... inputs) {
		return () -> {
			boolean stillTrue = false;
			for (BinaryInput input : inputs) {
				stillTrue = stillTrue || input.get();
			}
			return stillTrue;
		};
	}

	public static ListeningBinaryInput getListeningSource(BinaryInput in, Runnable runner) {
		return new ListeningBinaryInput(in, runner);
	}
}
