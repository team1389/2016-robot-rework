package com.team1389.hardware.inputs.interfaces;
/**
 * represents a stream of boolean values, and various options to manipulate them
 * @author Kenneth
 *
 */
public interface BinaryInput {
	/**
	 * 
	 * @return current value of stream
	 */
	public boolean get();
	/**
	 * 
	 * @param in stream of boolean
	 * @return
	 */
	public static BinaryInput invert(BinaryInput in) {
		return () -> {
			return !in.get();
		};
	}

	public static LatchedBinaryInput getLatched(BinaryInput in) {
		return new LatchedBinaryInput(in);
	}

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
