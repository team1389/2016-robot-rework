package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.inputs.interfaces.BinaryInput;

public class TrackedBinaryOutput implements BinaryOutput {
	boolean lastVal;
	BinaryOutput out;

	@Override
	public void set(boolean val) {
		this.lastVal = val;
		out.set(val);
	}

	public TrackedBinaryOutput(BinaryOutput out) {
		this.out = out;
	}

	public BinaryInput getAsInput() {
		return () -> {
			return lastVal;
		};
	}
}
