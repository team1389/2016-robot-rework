package com.team1389.hardware.outputs.interfaces;

/**
 * A component that can be turned on or off
 * 
 * @author Prinz
 */
public interface BinaryOutput {
	public void set(boolean onOrOff);

	public static BinaryOutput invert(BinaryOutput out) {
		return (boolean val) -> {
			out.set(!val);
		};
	}

	public static TrackedBinaryOutput getTracked(BinaryOutput out) {
		return new TrackedBinaryOutput(out);
	}
}
