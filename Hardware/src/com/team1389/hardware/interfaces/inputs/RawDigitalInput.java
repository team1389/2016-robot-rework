package com.team1389.hardware.interfaces.inputs;

public class RawDigitalInput extends DigitalInput{

	public RawDigitalInput(BooleanSource in) {
		super(in);
	}

	@Override
	public boolean get() {
		return in.get();
	}

}
