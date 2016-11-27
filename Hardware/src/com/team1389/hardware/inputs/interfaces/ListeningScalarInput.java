package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.valueTypes.Value;

public class ListeningScalarInput<T extends Value> implements ScalarInput<T>{
	private ScalarInput<T> in;
	private Runnable onChange;
	private double oldVal;

	protected ListeningScalarInput(ScalarInput<T> in, Runnable onChange) {
		this.onChange = onChange;
		this.in = in;
	}

	public double get() {
		double newVal = in.get();
		if (oldVal != newVal) {
			onChange.run();
		}
		oldVal = newVal;
		return newVal;
	}
}
