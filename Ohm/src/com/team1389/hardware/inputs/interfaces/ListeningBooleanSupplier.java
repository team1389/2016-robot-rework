package com.team1389.hardware.inputs.interfaces;

import java.util.function.BooleanSupplier;

public class ListeningBooleanSupplier implements BooleanSupplier {
	private BooleanSupplier in;
	private Runnable onChange;
	private boolean oldVal;

	protected ListeningBooleanSupplier(BooleanSupplier in, Runnable onChange) {
		this.onChange = onChange;
		this.in = in;
	}

	@Override
	public boolean getAsBoolean() {
		boolean newVal = in.getAsBoolean();
		if (oldVal != newVal) {
			onChange.run();
		}
		oldVal = newVal;
		return newVal;
	}
}
