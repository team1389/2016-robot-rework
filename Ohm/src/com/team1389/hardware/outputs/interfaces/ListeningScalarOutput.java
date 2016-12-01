package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.value_types.Value;

public class ListeningScalarOutput<T extends Value> implements ScalarOutput<T> {
	private ScalarOutput<T> out;
	private Runnable onChange;

	protected ListeningScalarOutput(ScalarOutput<T> out, Runnable onChange) {
		this.onChange = onChange;
		this.out = out;
	}

	public void set(double val) {
		out.set(val);
		onChange.run();
	}
}
