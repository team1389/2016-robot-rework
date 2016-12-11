package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.BinaryOutput;
import com.team1389.hardware.outputs.interfaces.TrackedBinaryOutput;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class DigitalOut {
	BinaryOutput out;

	public DigitalOut(BinaryOutput out) {
		this.out = out;
	}

	public void set(boolean onOrOff) {
		out.set(onOrOff);
	}

	public DigitalOut invert() {
		this.out = BinaryOutput.invert(out);
		return this;
	}

	public Watchable getWatchable(String name) {
		this.out = BinaryOutput.getTracked(out);
		return new BooleanInfo(name, ((TrackedBinaryOutput) out).getAsInput());
	}
}
