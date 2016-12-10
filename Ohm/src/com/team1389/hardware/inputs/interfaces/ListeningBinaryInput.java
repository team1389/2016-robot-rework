package com.team1389.hardware.inputs.interfaces;

public class ListeningBinaryInput implements BinaryInput {
	private BinaryInput in;
	private Runnable onChange;
	private boolean oldVal;

	protected ListeningBinaryInput(BinaryInput in, Runnable onChange) {
		this.onChange = onChange;
		this.in = in;
	}

	public boolean get() {
		boolean newVal = in.get();
		if (oldVal != newVal) {
			onChange.run();
		}
		oldVal = newVal;
		return newVal;
	}
}
