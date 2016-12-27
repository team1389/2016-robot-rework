package com.team1389.hardware.outputs.software;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import com.team1389.hardware.outputs.interfaces.BinaryOutput;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class DigitalOut implements BooleanSupplier {
	BinaryOutput out;
	private boolean last;

	public DigitalOut(BinaryOutput out) {
		this.out = out;
	}
	public DigitalOut(Consumer<Boolean> out){
		this(BinaryOutput.convert(out));
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
	public boolean getAsBoolean() {
		return last;
	}
}
