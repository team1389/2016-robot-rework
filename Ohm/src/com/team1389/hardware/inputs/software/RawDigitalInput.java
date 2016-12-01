package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.BooleanSource;

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
