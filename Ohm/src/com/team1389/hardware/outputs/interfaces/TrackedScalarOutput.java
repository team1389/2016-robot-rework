package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;

public class TrackedScalarOutput<T extends Value> implements ScalarOutput<T> {
	double lastSetVal;
	ScalarOutput<T> setter;

	public TrackedScalarOutput(ScalarOutput<T> setter) {
		this.setter = setter;
	}

	@Override
	public void set(double val) {
		lastSetVal = val;
		setter.set(val);
	}

	public double get() {
		return lastSetVal;
	}

	public ScalarInput<T> getAsInput() {
		return () -> {
			return get();
		};
	}

}
