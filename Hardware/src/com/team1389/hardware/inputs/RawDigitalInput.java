package com.team1389.hardware.inputs;

import com.team1389.hardware.interfaces.inputs.BooleanSource;

public class RawDigitalInput extends DigitalInput{

	public RawDigitalInput(BooleanSource in) {
		super(in);
	}

	public RawDigitalInput(BooleanSource in, String name) {
		super(in,name);
	}

	@Override
	public boolean get() {
		return in.get();
	}

}
