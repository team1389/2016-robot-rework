package com.team1389.hardware.outputs.software;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.outputs.interfaces.BinaryOutput;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class DigitalOut implements BinaryInput {
	BinaryOutput out;
	private boolean last;

	public DigitalOut(BinaryOutput out) {
		this.out = out;
	}

	public void set(boolean onOrOff) {
		this.last = onOrOff;
		out.set(onOrOff);
	}

	public DigitalOut invert() {
		this.out = BinaryOutput.invert(out);
		return this;
	}

	public Watchable getWatchable(String name) {
		return new BooleanInfo(name, this);
	}

	@Override
	public Boolean get() {
		return last;
	}
}
