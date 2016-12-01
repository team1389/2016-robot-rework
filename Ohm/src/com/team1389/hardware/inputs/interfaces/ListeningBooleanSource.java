package com.team1389.hardware.inputs.interfaces;

public class ListeningBooleanSource implements BooleanSource {
	private BooleanSource in;
	private Runnable onChange;
	private boolean oldVal;

	protected ListeningBooleanSource(BooleanSource in, Runnable onChange) {
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
